package com.example.ratbvkotlin.viewmodels

import androidx.lifecycle.*
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusStationModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BusStationsViewModel(private val _repository: BusRepository,
                           private val _directionLinkNormal: String,
                           private val _directionLinkReverse: String,
                           private val _busLineId: Int,
                           val busLineName: String)
    : ViewModel() {

    private val _isNormalDirection = MutableStateFlow(true)
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    val isNormalDirection: StateFlow<Boolean> = _isNormalDirection

    /**
     * Observes the bus station data from the repository as a Flow
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val busStations: StateFlow<List<BusStationItemViewModel>> =
        isNormalDirection.flatMapLatest { isNormalDirection ->
            _repository.observeBusStations(_busLineId.toLong(), )
                .map { busStationModels ->
                    busStationModels
                        .filter { busStationModel ->
                            busStationModel.direction ==
                                    if (isNormalDirection) "normal"
                                    else "reverse"
                        }
                        .map { busLineModel ->
                            BusStationItemViewModel(busLineModel)
                        }
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    // Sets the lastUpdated value based on the first item of the list since all will have the same value
    @OptIn(ExperimentalCoroutinesApi::class)
    val lastUpdated: StateFlow<String> = busStations.mapLatest { busStations ->
        busStations.firstOrNull()?.lastUpdateDate ?: "Never"
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = "Never"
    )

    init {
        viewModelScope.launch {
            refreshBusStations(isForcedRefresh = false)
        }
    }

    /**
     * Forces a refresh of the data in the database by doing a web fetch
     */
    suspend fun refreshBusStations(isForcedRefresh: Boolean) {

        _isRefreshing.value = true

        _repository.refreshBusStations(
            _busLineId.toLong(),
            _directionLinkNormal,
            _directionLinkReverse,
            isForcedRefresh)

        _isRefreshing.value = false
    }

    /**
     * Reverses the bus stations
     */
    fun reverseBusStationsDirection() {

        _isNormalDirection.value = !_isNormalDirection.value
    }

    /**
     * Downloads all timetables for all stations of specific bus line
     */
    suspend fun downloadStationsTimetables() {

        _isRefreshing.value = true

        _repository.downloadAllStationsTimetables(
            _directionLinkNormal,
            _directionLinkReverse,
            _busLineId)

        // Refreshes the list with the new inserted stations
        //getBusStations()

        _isRefreshing.value = false
    }

    /**
     * [ViewModel] for a specific [BusStationItemComponent], which contains the item's [busStation].
     */
    inner class BusStationItemViewModel(private val busStation: BusStationModel) : ViewModel() {

        val name: String = busStation.name
        val lastUpdateDate: String = busStation.lastUpdateDate

        // Navigation parameters
        val scheduleLink: String = busStation.scheduleLink
        val id: Int = busStation.id
    }
}