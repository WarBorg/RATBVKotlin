package com.example.ratbvkotlin.ui.busline

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusLineModel

class BusLinesViewModel(private val repository: BusRepository) : ViewModel() {

    // TODO use with livedata on fragment
    val busLines : LiveData<List<BusLineViewModel>> = liveData {
        val busLines = repository.getBusLines(true).map {
                busLineModel -> BusLineViewModel(busLineModel)
        }

        emit(busLines)
    }

    /**
     * [ViewModel] for a specific [FragmentBusLineBinding], which contains the item's [busLine]
     * and [onItemClicked] which is called when the users clicks on the current item.
     */
    /*inner*/ class BusLineViewModel(val busLine: BusLineModel) : ViewModel() {

        /*fun onItemClicked() {
            onItemClickListener?.invoke(forecast.date.time)
        }*/
    }
}