package com.example.ratbvkotlin.viewmodels

import androidx.lifecycle.*
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusStationModel
import kotlinx.coroutines.launch

class BusStationsViewModel(private val _repository: BusRepository,
                           private val _directionLinkNormal: String,
                           private val _directionLinkReverse: String,
                           private val _busLineId: Int,
                           val busLineName: String)
    : ViewModel() {

    private var _isNormalDirection = true
    private val _isRefreshing = MutableLiveData(false)
    private val _busStations = MutableLiveData<List<BusStationItemViewModel>>()

    val isRefreshing: LiveData<Boolean> = _isRefreshing
    val busStations: LiveData<List<BusStationItemViewModel>> = _busStations

    // Sets the lastUpdated value based on the first item of the list since all will have the same value
    val lastUpdated: LiveData<String> = Transformations.map(_busStations) { busStations ->
        busStations.firstOrNull()?.lastUpdateDate ?: "Never"
    }

    init {
        viewModelScope.launch {
            getBusStations()
        }
    }

    /**
     * Gets the bus stations data from the repository as LiveData
     */
    suspend fun getBusStations(isForcedRefresh: Boolean = false) {

        _isRefreshing.value = true

        val (directionLink, direction) = when (_isNormalDirection) {
            true -> Pair(_directionLinkNormal, "normal")
            false -> Pair(_directionLinkReverse, "reverse")
        }

        _busStations.value = _repository.getBusStations(
            directionLink,
            direction,
            _busLineId,
            isForcedRefresh)
            .map { busStationModel -> BusStationItemViewModel(busStationModel) }

        _isRefreshing.value = false
    }

    /**
     * Reverses the bus stations
     */
    suspend fun reverseStations() {

        _isNormalDirection = !_isNormalDirection

        getBusStations()
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
        getBusStations()

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