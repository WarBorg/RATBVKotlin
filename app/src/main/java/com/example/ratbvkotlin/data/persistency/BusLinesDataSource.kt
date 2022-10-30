package com.example.ratbvkotlin.data.persistency

import kotlinx.coroutines.flow.Flow
import ratbv.BusLineEntity

interface BusLinesDataSource {

    suspend fun countBusLines() : Long

    fun getBusLines(): Flow<List<BusLineEntity>>

    suspend fun saveBusLines(busLines: List<BusLineEntity>)

    suspend fun clearBusLines()
}