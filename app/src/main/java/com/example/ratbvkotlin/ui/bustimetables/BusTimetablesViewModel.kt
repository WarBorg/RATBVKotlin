package com.example.ratbvkotlin.ui.bustimetables

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusTimetableModel

class BusTimetablesViewModel(private val repository: BusRepository,
                             private val scheduleLink: String,
                             private val busStationId: Int,
                             private val timetableType: String)
    : ViewModel() {

    var lastUpdated = "Never"

    val busTimetables: LiveData<List<BusTimetableItemViewModel>> = liveData {
        val busTimetables = repository.getBusTimetables(scheduleLink,
                                                                                                      busStationId,
                                                                                        true)
            .filter { busTimetableModel -> busTimetableModel.timeOfWeek == timetableType }
            .map { busTimetableModel -> BusTimetableItemViewModel(busTimetableModel) }

        emit(busTimetables)
    }

     class BusTimetableItemViewModel (val busTimetable: BusTimetableModel) : ViewModel()
}