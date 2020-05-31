package com.example.ratbvkotlin.ui.busstations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusStationModel
import com.example.ratbvkotlin.databinding.FragmentBusStationListItemBinding

class BusStationsViewModel(private val repository: BusRepository,
                           private val directionLink: String,
                           private val direction: String,
                           private val busLineId: Int)
    : ViewModel() {

    private val _lastUpdated = MutableLiveData("Never")
    val lastUpdated: LiveData<String> = _lastUpdated

    /**
     * Listener set by [BusStationsFragment] in order to get notified when an [FragmentBusStationListItemBinding] is clicked.
     */
    var onBusStationClickListener: OnBusStationClickListener? = null

    val busStations : LiveData<List<BusStationsViewModel.BusStationItemViewModel>> = liveData {
        val busStations = repository.getBusStations(directionLink,
                                                                                              direction,
                                                                                              busLineId,
                                                                                true)
            .map { busStationModel -> BusStationItemViewModel(busStationModel) }


         // Sets the lastUpdated value based on the first item of the list since all will have the same value
        _lastUpdated.value = busStations.firstOrNull()?.busStation?.lastUpdateDate ?: "Never"

        emit(busStations)
    }

    /**
     * [ViewModel] for a specific [FragmentBusStationListItemBinding], which contains the item's [busStation]
     * and [onItemClicked] which is called when the users clicks on the current item.
     */
    inner class BusStationItemViewModel(val busStation: BusStationModel) : ViewModel() {

        fun onItemClicked() {
            onBusStationClickListener?.invoke(busStation.scheduleLink, busStation.id)
        }
    }
}

/**
 * Shortcut for a method call when a [FragmentBusStationListItemBinding] is clicked.
 */
typealias OnBusStationClickListener = (scheduleLink: String, busStationId: Int) -> Unit