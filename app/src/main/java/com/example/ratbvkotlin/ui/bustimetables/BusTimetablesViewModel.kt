package com.example.ratbvkotlin.ui.bustimetables

import androidx.lifecycle.*
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusTimetableModel
import com.example.ratbvkotlin.databinding.FragmentBusTimetableListItemBinding

class BusTimetablesViewModel(private val repository: BusRepository,
                             private val scheduleLink: String,
                             private val busStationId: Int,
                             val busStationName: String,
                             private val timetableType: String)
    : ViewModel() {

    private val _isRefreshing = MutableLiveData(false)
    private val _busTimetables = MutableLiveData<List<BusTimetableItemViewModel>>()

    val isRefreshing: LiveData<Boolean> = _isRefreshing
    val busTimetables: LiveData<List<BusTimetableItemViewModel>> = _busTimetables

    // Sets the lastUpdated value based on the first item of the list since all will have the same value
    val lastUpdated: LiveData<String> = Transformations.map(_busTimetables) { busTimetables ->
        busTimetables.firstOrNull()?.busTimetable?.lastUpdateDate ?: "Never"
    }

    /**
     * Gets the bus stations data from the repository as LiveData
     */
    suspend fun getBusTimetables(isForcedRefresh: Boolean = false) {

        _isRefreshing.value = true

        _busTimetables.value = repository.getBusTimetables(scheduleLink,
            busStationId,
            isForcedRefresh)
            .filter { busTimetableModel -> busTimetableModel.timeOfWeek == timetableType }
            .map { busTimetableModel -> BusTimetableItemViewModel(busTimetableModel) }

        _isRefreshing.value = false
    }

    /**
     * [ViewModel] for a specific [FragmentBusTimetableListItemBinding], which contains the item's [busTimetable]
     */
     class BusTimetableItemViewModel (val busTimetable: BusTimetableModel) : ViewModel()
}