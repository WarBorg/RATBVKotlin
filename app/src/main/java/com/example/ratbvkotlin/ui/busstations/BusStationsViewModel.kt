package com.example.ratbvkotlin.ui.busstations

import androidx.lifecycle.ViewModel
import com.example.ratbvkotlin.data.BusRepository

class BusStationsViewModel(private val repository: BusRepository,
                           private val busLineId: Int)
    : ViewModel() {
}