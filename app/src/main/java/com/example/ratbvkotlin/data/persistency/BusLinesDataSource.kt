package com.example.ratbvkotlin.data.persistency

import kotlinx.coroutines.flow.Flow
import ratbv.BusLineEntity

interface BusLinesDataSource {

    suspend fun countBusLines(): Long

    fun getBusLines(): Flow<List<BusLineEntity>>

    suspend fun saveBusLines(
        name: String,
        route: String,
        type: String,
        linkNormalWay: String,
        linkReverseWay: String,
        lastUpdateDate: String,
        id: Long?
    )

    suspend fun clearBusLines()
}