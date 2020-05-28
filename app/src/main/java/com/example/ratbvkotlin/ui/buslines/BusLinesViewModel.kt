package com.example.ratbvkotlin.ui.buslines

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusLineModel
import com.example.ratbvkotlin.databinding.FragmentBusLineListItemBinding

class BusLinesViewModel(private val repository: BusRepository,
                        private val busTransportSubtype: String)
    : ViewModel() {

    /**
     * Listener set by [BusLinesFragment] in order to get notified when an [FragmentBusLineListItemBinding] is clicked.
     */
    var onBusLineClickListener: OnBusLineClickListener? = null

    var lastUpdated = "Never"

    val busLines : LiveData<List<BusLineItemViewModel>> = liveData {
        val busLines = repository.getBusLines(true)
            .filter { busLineModel -> busLineModel.type == busTransportSubtype }
            .map { busLineModel -> BusLineItemViewModel(busLineModel) }

        emit(busLines)
    }

    /**
     * [ViewModel] for a specific [FragmentBusLineListItemBinding], which contains the item's [busLine]
     * and [onItemClicked] which is called when the users clicks on the current item.
     */
    inner class BusLineItemViewModel(val busLine: BusLineModel) : ViewModel() {

        fun onItemClicked() {
            onBusLineClickListener?.invoke(busLine.linkNormalWay,"normal", busLine.id)
        }
    }
}

/**
 * Shortcut for a method call when a [FragmentBusLineListItemBinding] is clicked.
 */
typealias OnBusLineClickListener = (directionLink: String, direction: String, busLineId: Int) -> Unit