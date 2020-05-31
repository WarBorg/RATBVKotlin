package com.example.ratbvkotlin.ui.buslines

import androidx.lifecycle.*
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusLineModel
import com.example.ratbvkotlin.databinding.FragmentBusLineListItemBinding

class BusLinesViewModel(private val repository: BusRepository,
                        private val busTransportSubtype: String)
    : ViewModel() {

    private val _isRefreshing = MutableLiveData(false)
    private val _busLines = MutableLiveData<List<BusLineItemViewModel>>()

    val isRefreshing: LiveData<Boolean> = _isRefreshing
    val busLines: LiveData<List<BusLineItemViewModel>> = _busLines

    // Sets the lastUpdated value based on the first item of the list since all will have the same value
    val lastUpdated: LiveData<String> = Transformations.map(_busLines) { busLines ->
        busLines.firstOrNull()?.busLine?.lastUpdateDate ?: "Never"
    }

    /**
     * Listener set by [BusLinesFragment] in order to get notified when an [FragmentBusLineListItemBinding] is clicked.
     */
    var onBusLineClickListener: OnBusLineClickListener? = null


    /**
     * Gets the bus lines data from the repository as LiveData
     */
    suspend fun refreshBusLines() {

        _isRefreshing.value = true

        _busLines.value = repository.getBusLines(true)
            .filter { busLineModel -> busLineModel.type == busTransportSubtype }
            .map { busLineModel -> BusLineItemViewModel(busLineModel) }

        _isRefreshing.value = false
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