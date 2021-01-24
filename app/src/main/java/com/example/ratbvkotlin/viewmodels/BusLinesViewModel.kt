package com.example.ratbvkotlin.viewmodels

import androidx.lifecycle.*
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusLineModel

class BusLinesViewModel(private val _repository: BusRepository)
    : ViewModel() {

    private val _isRefreshing = MutableLiveData(false)
    private val _busLines = MutableLiveData<List<BusLineItemViewModel>>()

    val isRefreshing: LiveData<Boolean> = _isRefreshing
    val busLines: LiveData<List<BusLineItemViewModel>> = _busLines

    // Sets the lastUpdated value based on the first item of the list since all will have the same value
    val lastUpdated: LiveData<String> = Transformations.map(_busLines) { busLines ->
        busLines.firstOrNull()?.lastUpdateDate ?: "Never"
    }

    /**
     * Gets the bus lines data from the repository as LiveData
     */
    suspend fun getBusLines(busTransportSubtype: String,
                            isForcedRefresh: Boolean = false) {

        _isRefreshing.value = true

        _busLines.value = _repository.getBusLines(isForcedRefresh)
            .filter { busLineModel -> busLineModel.type == busTransportSubtype }
            .map { busLineModel -> BusLineItemViewModel(busLineModel) }

        _isRefreshing.value = false
    }

    /**
     * [ViewModel] for a specific [BusTimetableItemComponent], which contains the item's [busLine].
     */
    inner class BusLineItemViewModel(private val busLine: BusLineModel) : ViewModel() {

        val name: String = busLine.name
        val route: String = busLine.route
        val lastUpdateDate: String = busLine.lastUpdateDate

        // Navigation parameters
        val id: Int = busLine.id
        val linkNormalWay: String = busLine.linkNormalWay
        val linkReverseWay: String = busLine.linkReverseWay
    }
}