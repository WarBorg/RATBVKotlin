package com.example.ratbvkotlin.data.persistency

import com.example.ratbvkotlin.RatbvDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ratbv.BusStationEntity

class BusStationsDataSourceImpl(databaseDriverFactory: DatabaseDriverFactory)
    : BusStationsDataSource {

    private val database =
        RatbvDatabase.invoke(
            databaseDriverFactory.createDriver()
        )

    private val queries = database.busStationEntityQueries

    override suspend fun countBusStationsByBusLineId(busLineId: Long): Long {
        return withContext(Dispatchers.IO) {
            queries.countBusStationsByBusLineId(busLineId)
                .executeAsOne()
        }
    }

    override fun getBusStationsByBusLineId(busLineId: Long)
    : Flow<List<BusStationEntity>> {
        return queries.getBusStationsByBusLineId(busLineId)
            .asFlow()
            .mapToList()
    }

    override suspend fun saveBusStations(busStations: List<BusStationEntity>) {
        return withContext(Dispatchers.IO) {
            queries.transaction {
                busStations.forEach { busStation ->
                    queries.saveBusStation(
                        if (busStation.id == -1L) null else busStation.id,
                        busStation.busLineId,
                        busStation.name,
                        busStation.direction,
                        busStation.scheduleLink,
                        busStation.lastUpdateDate,
                    )
                }
            }
        }
    }

    override suspend fun clearBusStationsByBusLineId(busLineId: Long) {
        return withContext(Dispatchers.IO) {
            queries.clearBusStationsByBusLineId(busLineId)
        }
    }
}