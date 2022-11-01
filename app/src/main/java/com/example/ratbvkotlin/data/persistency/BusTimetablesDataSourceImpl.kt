package com.example.ratbvkotlin.data.persistency

import com.example.ratbvkotlin.RatbvDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ratbv.BusTimetableEntity

class BusTimetablesDataSourceImpl (databaseDriverFactory: DatabaseDriverFactory)
    : BusTimetablesDataSource {

    private val database =
        RatbvDatabase.invoke(
            databaseDriverFactory.createDriver()
        )

    private val queries = database.busTimetableEntityQueries

    override suspend fun countBusTimetablesByBusStationId(busStationId: Long): Long {
        return queries.countBusTimetablesByBusStationId(busStationId)
            .executeAsOne()
    }

    override fun getBusTimetablesByBusStationId(busStationId: Long)
    : Flow<List<BusTimetableEntity>> {
        return queries.getBusTimetablesByBusStationId(busStationId)
            .asFlow()
            .mapToList()
    }

    override suspend fun saveBusTimetables(busTimetables: List<BusTimetableEntity>) {
        return withContext(Dispatchers.IO) {
            queries.transaction {
                busTimetables.forEach { busTimetable ->
                    queries.saveBusTimetable(
                        if (busTimetable.id == -1L) null else busTimetable.id,
                        busTimetable.busStationId,
                        busTimetable.hour,
                        busTimetable.minutes,
                        busTimetable.timeOfWeek,
                        busTimetable.lastUpdateDate,
                    )
                }
            }
        }
    }

    override suspend fun clearBusTimetablesByBusStationId(busStationId: Long) {
        return withContext(Dispatchers.IO) {
            queries.clearBusTimetablesByBusStationId(busStationId)
        }
    }
}