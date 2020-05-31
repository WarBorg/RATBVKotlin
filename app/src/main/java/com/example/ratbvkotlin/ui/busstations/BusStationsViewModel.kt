package com.example.ratbvkotlin.ui.busstations

import androidx.lifecycle.*
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusStationModel
import com.example.ratbvkotlin.databinding.FragmentBusStationListItemBinding

class BusStationsViewModel(private val repository: BusRepository,
                           private val directionLink: String,
                           private val direction: String,
                           private val busLineId: Int,
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

    /**
     * Listener set by [BusStationsFragment] in order to get notified when an [FragmentBusStationListItemBinding] is clicked.
     */
    var onBusStationClickListener: OnBusStationClickListener? = null

    /**
     * Gets the bus stations data from the repository as LiveData
     */
    suspend fun getBusStations() {

        _isRefreshing.value = true

        _busStations.value = repository.getBusStations(
            directionLink,
            direction,
            busLineId,
            true)
            .map { busStationModel -> BusStationItemViewModel(busStationModel) }

        _isRefreshing.value = false
    }

    /**
     * [ViewModel] for a specific [FragmentBusStationListItemBinding], which contains the item's [busStation]
     * and [onItemClicked] which is called when the users clicks on the current item.
     */
    inner class BusStationItemViewModel(val busStation: BusStationModel) : ViewModel() {

        fun onItemClicked() {
            onBusStationClickListener?.invoke(busStation.scheduleLink, busStation.id, busStation.name)
        }
    }
}

/**
 * Shortcut for a method call when a [FragmentBusStationListItemBinding] is clicked.
 */
typealias OnBusStationClickListener = (scheduleLink: String,
                                       busStationId: Int,
                                       busStationName: String) -> Unit