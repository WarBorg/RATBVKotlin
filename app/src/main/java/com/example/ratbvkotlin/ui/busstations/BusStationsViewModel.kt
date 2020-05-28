package com.example.ratbvkotlin.ui.busstations

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusStationModel

class BusStationsViewModel(private val repository: BusRepository,
                           private val directionLink: String,
                           private val direction: String,
                           private val busLineId: Int)
    : ViewModel() {

    /**
     * Listener set by [BusStationsFragment] in order to get notified when an [FragmentBusStationBinding] is clicked.
     */
    var onBusStationClickListener: OnBusStationClickListener? = null

    val busStations : LiveData<List<BusStationsViewModel.BusStationItemViewModel>> = liveData {
        val busStations = repository.getBusStations(directionLink,
                                                                                              direction,
                                                                                              busLineId,
                                                                                true)
            .map { busStationModel -> BusStationItemViewModel(busStationModel) }

        emit(busStations)
    }

    /**
     * [ViewModel] for a specific [FragmentBusStationListItemBinding], which contains the item's [busStation]
     * and [onItemClicked] which is called when the users clicks on the current item.
     */
    inner class BusStationItemViewModel(val busStation: BusStationModel) : ViewModel() {

        fun onItemClicked() {
            onBusStationClickListener?.invoke(busStation.id)
        }
    }
}

/**
 * Shortcut for a method call when a [FragmentBusStationBinding] is clicked.
 */
typealias OnBusStationClickListener = (busStationId: Int) -> Unit