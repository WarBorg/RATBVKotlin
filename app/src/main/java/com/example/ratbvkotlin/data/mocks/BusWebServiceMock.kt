package com.example.ratbvkotlin.data.mocks

import com.example.ratbvkotlin.data.dtos.BusLineDto
import com.example.ratbvkotlin.data.dtos.BusStationDto
import com.example.ratbvkotlin.data.dtos.BusTimetableDto
import com.example.ratbvkotlin.data.interfaces.IBusWebservice

class BusWebServiceMock : IBusWebservice {

    override suspend fun getBusLines(): List<BusLineDto> = listOf(
        BusLineDto(1, "test1", "route 1", "", "", ""),
        BusLineDto(2, "test2", "route 2", "", "", ""),
        BusLineDto(3, "test3", "route 3", "", "", ""),
        BusLineDto(4, "test4", "route 4", "", "", "")
    )

    override suspend fun getBusStations(lineNumberLink: String): List<BusStationDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getBusTimetables(scheduleLink: String): List<BusTimetableDto> {
        TODO("Not yet implemented")
    }
}