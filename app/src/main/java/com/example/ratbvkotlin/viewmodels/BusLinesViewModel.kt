package com.example.ratbvkotlin.viewmodels

import androidx.lifecycle.*
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusLineModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch

enum class BusTransportSubtypes {
    Bus, Electricbus, Trolleybus
}

class BusLinesViewModel(private val _repository: BusRepository)
    : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    private val _busTransportSubtype = MutableStateFlow(BusTransportSubtypes.Bus)

    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    val busTransportSubtype: StateFlow<BusTransportSubtypes> = _busTransportSubtype

    /**
     * Observes the bus lines data from the repository as a Flow
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val busLines: SharedFlow<List<BusLineItemViewModel>> =
        busTransportSubtype.flatMapLatest { busTransportSubtype ->
            _repository.observeBusLines()
                .map { busLineModels ->
                    busLineModels
                        .filter { busLineModel ->
                            busLineModel.type == busTransportSubtype.name
                        }
                        .map { busLineModel ->
                            BusLineItemViewModel(busLineModel)
                        }
                }
        }.shareIn(
            scope = viewModelScope,
            started = WhileSubscribed(stopTimeoutMillis = 5000)
        )

    // Sets the lastUpdated value based on the first item of the list since all will have the same value
    @OptIn(ExperimentalCoroutinesApi::class)
    val lastUpdated: StateFlow<String> = busLines.mapLatest { busLines ->
        busLines.firstOrNull()?.lastUpdateDate ?: "Never"
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = "Never"
    )

    init {
        viewModelScope.launch {
            refreshBusLines(isForcedRefresh = false)
        }
    }

    /**
     * Forces a refresh of the data in the database by doing a web fetch
     */
    suspend fun refreshBusLines(isForcedRefresh: Boolean) {
        _isRefreshing.value = true

        _repository.refreshBusLines(isForcedRefresh)

        _isRefreshing.value = false
    }

    /**
     * Updates the bus type based on the bottom tab being viewed
     */
    fun updateViewedBusLineSubtype(busTransportSubtype: BusTransportSubtypes) {
        _busTransportSubtype.value = busTransportSubtype
    }

    /**
     * [ViewModel] for a specific [BusLineItemViewModel], which contains the item's [busLine].
     */
    inner class BusLineItemViewModel(private val busLine: BusLineModel) : ViewModel() {

        val name: String = busLine.name
        val route: String = busLine.route
        val lastUpdateDate: String = busLine.lastUpdateDate

        // Navigation parameters
        val id: Long = busLine.id
        val linkNormalWay: String = busLine.linkNormalWay
        val linkReverseWay: String = busLine.linkReverseWay
    }
}