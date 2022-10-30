package com.example.ratbvkotlin.data.persistency

import com.example.ratbvkotlin.RatbvDatabase
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
        return queries.countBusLines().executeAsOne()
    }

    override fun getBusLines(): Flow<List<BusLineEntity>> {
        return queries.getBusLines().asFlow().mapToList()
    }

    override suspend fun saveBusLines(
        name: String,
        route: String,
        type: String,
        linkNormalWay: String,
        linkReverseWay: String,
        lastUpdateDate: String,
        id: Long?
    ) {
        return withContext(Dispatchers.IO) {
            queries.saveBusLines(id, name, route, type, linkNormalWay, linkReverseWay, lastUpdateDate)
        }
    }

    override suspend fun clearBusLines() {
        return withContext(Dispatchers.IO) {
            queries.clearBusLines()
        }
    }
}