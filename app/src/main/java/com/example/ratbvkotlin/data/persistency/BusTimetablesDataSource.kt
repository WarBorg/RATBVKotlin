package com.example.ratbvkotlin.data.persistency

import kotlinx.coroutines.flow.Flow
import ratbv.BusTimetableEntity

interface BusTimetablesDataSource {

    suspend fun countBusTimetablesByBusStationId(busStationId: Long): Long

    fun getBusTimetablesByBusStationId(busStationId: Long): Flow<List<BusTimetableEntity>>

    suspend fun saveBusTimetables(busTimetables: List<BusTimetableEntity>)

    suspend fun clearBusTimetablesByBusStationId(busStationId: Long)
}