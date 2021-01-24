package com.example.ratbvkotlin.ui.bustimetables

import androidx.lifecycle.*
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusTimetableModel

class BusTimetablesViewModel(private val _repository: BusRepository,
                             private val _scheduleLink: String,
                             private val _busStationId: Int,
                             val busStationName: String)
    : ViewModel() {

    private val _isRefreshing = MutableLiveData(false)
    private val _busTimetables = MutableLiveData<List<BusTimetableItemViewModel>>()

    val isRefreshing: LiveData<Boolean> = _isRefreshing
    val busTimetables: LiveData<List<BusTimetableItemViewModel>> = _busTimetables

    // Sets the lastUpdated value based on the first item of the list since all will have the same value
    val lastUpdated: LiveData<String> = Transformations.map(_busTimetables) { busTimetables ->
        busTimetables.firstOrNull()?.lastUpdateDate ?: "Never"
    }

    /**
     * Gets the bus stations data from the repository as LiveData
     */
    suspend fun getBusTimetables(timetableType: String,
                                 isForcedRefresh: Boolean = false) {

        _isRefreshing.value = true

        _busTimetables.value = _repository.getBusTimetables(_scheduleLink,
            _busStationId,
            isForcedRefresh)
            .filter { busTimetableModel -> busTimetableModel.timeOfWeek == timetableType }
            .map { busTimetableModel -> BusTimetableItemViewModel(busTimetableModel) }

        _isRefreshing.value = false
    }

    /**
     * [ViewModel] for a specific [FragmentBusTimetableListItemBinding], which contains the item's [busTimetable]
     */
     class BusTimetableItemViewModel (private val busTimetable: BusTimetableModel)
        : ViewModel() {

            val hour: String = busTimetable.hour
            val minutes: String = busTimetable.minutes
            val lastUpdateDate: String = busTimetable.lastUpdateDate
        }
}