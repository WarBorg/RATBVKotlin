package com.example.ratbvkotlin.viewmodels

import androidx.lifecycle.*
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusLineModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

enum class BusTransportSubtypes {
    Bus, Electricbus, Trolleybus
}

class BusLinesViewModel(private val _repository: BusRepository)
    : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    private val _busTransportSubtype = MutableStateFlow(BusTransportSubtypes.Bus)
    private val _busLines = MutableStateFlow<List<BusLineItemViewModel>>(emptyList())

    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    val busTransportSubtype: StateFlow<BusTransportSubtypes> = _busTransportSubtype
    val busLines: StateFlow<List<BusLineItemViewModel>> = _busLines

    // Sets the lastUpdated value based on the first item of the list since all will have the same value
    @OptIn(ExperimentalCoroutinesApi::class)
    val lastUpdated: StateFlow<String> = _busLines.mapLatest { busLines ->
        busLines.firstOrNull()?.lastUpdateDate ?: "Never"
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = "Never"
    )

    /**
     * Gets the bus lines data from the repository as Flow
     */
    suspend fun getBusLines(busTransportSubtype: BusTransportSubtypes,
                            isForcedRefresh: Boolean = false) {

        _isRefreshing.value = true

        _busTransportSubtype.value = busTransportSubtype

        _busLines.value = _repository.getBusLines(isForcedRefresh)
            .filter { busLineModel -> busLineModel.type == busTransportSubtype.name }
            .map { busLineModel -> BusLineItemViewModel(busLineModel) }

        _isRefreshing.value = false
    }

    /**
     * [ViewModel] for a specific [BusLineItemViewModel], which contains the item's [busLine].
     */
    inner class BusLineItemViewModel(private val busLine: BusLineModel) : ViewModel() {

        val name: String = busLine.name
        val route: String = busLine.route
        val lastUpdateDate: String = busLine.lastUpdateDate

        // Navigation parameters
        val id: Int = busLine.id
        val linkNormalWay: String = busLine.linkNormalWay
        val linkReverseWay: String = busLine.linkReverseWay
    }
}