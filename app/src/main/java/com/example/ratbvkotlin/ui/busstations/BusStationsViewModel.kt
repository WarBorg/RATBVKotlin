package com.example.ratbvkotlin.ui.busstations

import androidx.lifecycle.ViewModel
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusStationModel

class BusStationsViewModel(private val repository: BusRepository,
                           private val busLineId: Int)
    : ViewModel() {

    /**
     * Listener set by [BusStationsFragment] in order to get notified when an [FragmentBusStationBinding] is clicked.
     */
    var onBusStationClickListener: OnBusStationClickListener? = null

    /**
     * [ViewModel] for a specific [FragmentBusStationBinding], which contains the item's [busStation]
     * and [onItemClicked] which is called when the users clicks on the current item.
     */
    inner class BusStationViewModel(val busStation: BusStationModel) : ViewModel() {

        fun onItemClicked() {
            onBusStationClickListener?.invoke(busStation.id)
        }
    }
}

/**
 * Shortcut for a method call when a [FragmentBusStationBinding] is clicked.
 */
typealias OnBusStationClickListener = (busStationId: Int) -> Unit