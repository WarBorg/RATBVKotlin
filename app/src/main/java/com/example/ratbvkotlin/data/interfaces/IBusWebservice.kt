package com.example.ratbvkotlin.data.interfaces

import com.example.ratbvkotlin.data.dtos.BusLineDto
import com.example.ratbvkotlin.data.dtos.BusStationDto
import com.example.ratbvkotlin.data.dtos.BusTimetableDto

interface IBusWebservice {
    suspend fun getBusLines(): List<BusLineDto>
    suspend fun getBusStations(lineNumberLink: String): List<BusStationDto>
    suspend fun getBusTimetables(scheduleLink: String): List<BusTimetableDto>
}