package com.example.ratbvkotlin.ui.bustimetables

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusTimetableModel

class BusTimetablesViewModel(private val repository: BusRepository,
                             private val scheduleLink: String,
                             private val busStationId: Int,
                             private val timetableType: String)
    : ViewModel() {

    private val _lastUpdated = MutableLiveData("Never")
    val lastUpdated: LiveData<String> = _lastUpdated

    val busTimetables: LiveData<List<BusTimetableItemViewModel>> = liveData {
        val busTimetables = repository.getBusTimetables(scheduleLink,
                                                                                                      busStationId,
                                                                                        true)
            .filter { busTimetableModel -> busTimetableModel.timeOfWeek == timetableType }
            .map { busTimetableModel -> BusTimetableItemViewModel(busTimetableModel) }

        // Sets the lastUpdated value based on the first item of the list since all will have the same value
        _lastUpdated.value = busTimetables.firstOrNull()?.busTimetable?.lastUpdateDate ?: "Never"

        emit(busTimetables)
    }

     class BusTimetableItemViewModel (val busTimetable: BusTimetableModel) : ViewModel()
}