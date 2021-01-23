package com.example.ratbvkotlin.ui.busstations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusStationModel

class BusStationsViewModel(private val _repository: BusRepository,
                           private val _directionLinkNormal: String,
                           private val _directionLinkReverse: String,
                           private val _busLineId: Int,
                           val busLineName: String)
    : ViewModel() {

    private val _isRefreshing = MutableLiveData(false)
    private val _busStations = MutableLiveData<List<BusStationItemViewModel>>()

    val isRefreshing: LiveData<Boolean> = _isRefreshing
    val busStations: LiveData<List<BusStationItemViewModel>> = _busStations

    // Sets the lastUpdated value based on the first item of the list since all will have the same value
    val lastUpdated: LiveData<String> = Transformations.map(_busStations) { busStations ->
        busStations.firstOrNull()?.busStation?.lastUpdateDate ?: "Never"
    }

    var isNormalDirection = true

    /**
     * Gets the bus stations data from the repository as LiveData
     */
    suspend fun getBusStations(isForcedRefresh: Boolean = false) {

        _isRefreshing.value = true

        val (directionLink, direction) = when (isNormalDirection) {
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

        isNormalDirection = !isNormalDirection

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
     * [ViewModel] for a specific [FragmentBusStationListItemBinding], which contains the item's [busStation]
     * and [onItemClicked] which is called when the users clicks on the current item.
     */
    inner class BusStationItemViewModel(val busStation: BusStationModel) : ViewModel() {

        val scheduleLink: String = busStation.scheduleLink
        val busStationId: Int = busStation.id
        val busStationName: String = busStation.name
    }
}