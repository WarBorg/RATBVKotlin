package com.example.ratbvkotlin.data.persistency

import com.example.ratbvkotlin.RatbvDatabase
import com.example.ratbvkotlin.data.models.BusLineModel
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ratbv.BusLineEntity

class BusLinesDataSourceImpl(databaseDriverFactory: DatabaseDriverFactory)
    : BusLinesDataSource {

    private val database =
        RatbvDatabase.invoke(
            databaseDriverFactory.createDriver()
        )

    private val queries = database.busLineEntityQueries

    override suspend fun countBusLines(): Long {
        return withContext(Dispatchers.IO) {
            queries.countBusLines()
                .executeAsOne()
        }
    }

    override fun getBusLines(): Flow<List<BusLineEntity>> {
        return queries.getBusLines().asFlow().mapToList()
    }

    override suspend fun saveBusLines(busLines: List<BusLineEntity>) {

        return withContext(Dispatchers.IO) {
            queries.transaction {
                busLines.forEach { busLine ->
                    queries.saveBusLine(busLine)
                }
            }
        }
    }

    override suspend fun clearBusLines() {
        return withContext(Dispatchers.IO) {
            queries.clearBusLines()
        }
    }
}