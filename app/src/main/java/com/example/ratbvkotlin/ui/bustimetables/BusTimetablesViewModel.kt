package com.example.ratbvkotlin.ui.bustimetables

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusTimetableModel

class BusTimetablesViewModel(private val repository: BusRepository,
                             private val schuduleLink: String,
                             private val busStationId: Int,
                             private val timetableType: String)
    : ViewModel() {

    val busTimetables: LiveData<List<BusTimetableItemViewModel>> = liveData {
        val busTimetables = repository.getBusTimetables(schuduleLink,
                                                                                                      busStationId,
                                                                                        true)
            .filter { busTimetableModel -> busTimetableModel.timeOfWeek == timetableType }
            .map { busTimetableModel -> BusTimetableItemViewModel(busTimetableModel) }
    }

    inner class BusTimetableItemViewModel (val busTimetable: BusTimetableModel) : ViewModel()
}