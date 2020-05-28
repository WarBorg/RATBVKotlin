package com.example.ratbvkotlin.ui.bustimetables

import androidx.lifecycle.ViewModel
import com.example.ratbvkotlin.data.BusRepository

class BusTimetablesViewModel(private val repository: BusRepository,
                             private val schuduleLink: String,
                             private val busStationId: Int,
                             private val timetableType: String)
    : ViewModel() {

    val busTimetables: Unit = Unit
}

/**
 * Shortcut for a method call when a [FragmentBusTimetablesBinding] is clicked.
 */
typealias OnBusStationClickListener = (busStationId: Int) -> Unit