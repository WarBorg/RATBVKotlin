package com.example.ratbvkotlin.data.interfaces

import com.example.ratbvkotlin.data.models.BusLineModel
import com.example.ratbvkotlin.data.models.BusStationModel
import com.example.ratbvkotlin.data.models.BusTimetableModel

interface IBusWebservice {
    suspend fun getBusLines(): List<BusLineModel>
    suspend fun getBusStations(lineNumberLink: String): List<BusStationModel>
    suspend fun getBusTimetables(scheduleLink: String): List<BusTimetableModel>
}