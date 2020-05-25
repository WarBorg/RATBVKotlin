package com.example.ratbvkotlin.ui.buslines

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusLineModel
import com.example.ratbvkotlin.databinding.FragmentBusLineBinding

class BusLinesViewModel(private val repository: BusRepository,
                        private val busTransportSubtype: String)
    : ViewModel() {

    /**
     * Listener set by [BusLinesFragment] in order to get notified when an [FragmentBusLineBinding] is clicked.
     */
    var onBusLineClickListener: OnBusLineClickListener? = null

    val busLines : LiveData<List<BusLineViewModel>> = liveData {
        val busLines = repository.getBusLines(true)
            .filter { busLineModel -> busLineModel.type == busTransportSubtype }
            .map { busLineModel -> BusLineViewModel(busLineModel) }

        emit(busLines)
    }

    /**
     * [ViewModel] for a specific [FragmentBusLineBinding], which contains the item's [busLine]
     * and [onItemClicked] which is called when the users clicks on the current item.
     */
    inner class BusLineViewModel(val busLine: BusLineModel) : ViewModel() {

        fun onItemClicked() {
            onBusLineClickListener?.invoke(busLine.id)
        }
    }
}

/**
 * Shortcut for a method call when a [FragmentBusLineBinding] is clicked.
 */
typealias OnBusLineClickListener = (busLineId: Int) -> Unit