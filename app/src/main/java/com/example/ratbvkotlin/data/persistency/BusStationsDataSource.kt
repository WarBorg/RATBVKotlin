package com.example.ratbvkotlin.data.persistency

import kotlinx.coroutines.flow.Flow
import ratbv.BusStationEntity

interface BusStationsDataSource {

    suspend fun countBusStationsByBusLineId(busLineId: Long): Long

    fun getBusStationsByBusLineId(busLineId: Long): Flow<List<BusStationEntity>>

    suspend fun saveBusStations(busStations: List<BusStationEntity>)

    suspend fun clearBusStationsByBusLineId(busLineId: Long)
}