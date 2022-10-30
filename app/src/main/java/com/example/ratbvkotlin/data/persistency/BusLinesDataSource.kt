package com.example.ratbvkotlin.data.persistency

import com.example.ratbvkotlin.data.models.BusLineModel
import kotlinx.coroutines.flow.Flow
import ratbv.BusLineEntity

interface BusLinesDataSource {

    fun getBusLines(): Flow<List<BusLineEntity>>

    suspend fun saveBusLines(busLines: List<BusLineEntity>)

    suspend fun clearBusLines()
}