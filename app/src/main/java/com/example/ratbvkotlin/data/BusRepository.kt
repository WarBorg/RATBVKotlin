package com.example.ratbvkotlin.data

import androidx.lifecycle.LiveData
import com.example.ratbvkotlin.data.interfaces.IBusWebservice
import com.example.ratbvkotlin.data.models.BusLineModel
import com.example.ratbvkotlin.data.models.BusStationModel
import com.example.ratbvkotlin.data.models.BusTimetableModel
import java.text.SimpleDateFormat
import java.util.*

class BusRepository(
    // TODO Maybe I should send the whole database class here instead of individual DAO's
    private val busLinesDao: BusLinesDao,
    private val busStationsDao: BusStationsDao,
    private val busTimetablesDao: BusTimetablesDao,
    private val busWebService: IBusWebservice
) {

    suspend fun getBusLines(isForcedRefresh: Boolean): List<BusLineModel> {

        val busLinesCount = busLinesDao.countBusLines()

        if (isForcedRefresh || busLinesCount == 0) {

            val busLines = busWebService.getBusLines()
            val current = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val lastUpdated = formatter.format(current)

            insertBusLinesInDatabase(busLines, lastUpdated)
        }

        return busLinesDao.getBusLines()
    }

    suspend fun getBusStations(directionLink: String,
                               direction: String,
                               busLineId: Int,
                               isForcedRefresh: Boolean)
            : LiveData<List<BusStationModel>> {

        val busStationsCount = busStationsDao.countBusStationsByBusLineIdAndDirection(busLineId, direction)

        if (isForcedRefresh || busStationsCount == 0) {

            val busStations = busWebService.getBusStations(directionLink)
            val current = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val lastUpdated = formatter.format(current)

            insertBusStationsInDatabase(busStations, busLineId, lastUpdated, direction)
        }

        return busStationsDao.getBusStationsByBusLineIdAndDirection(busLineId, direction)
    }

    suspend fun getBusTimetables(scheduleLink: String,
                                 busStationId: Int,
                                 isForcedRefresh: Boolean)
            : LiveData<List<BusTimetableModel>> {

        val busTimetablesCount = busTimetablesDao.countBusTimetablesByBusStationId(busStationId)

        if (isForcedRefresh || busTimetablesCount == 0) {

            val busTimetables = busWebService.getBusTimetables(scheduleLink)
            val current = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val lastUpdated = formatter.format(current)

            insertBusTimetablesInDatabase(busTimetables, busStationId, lastUpdated)
        }

        return busTimetablesDao.getBusTimetablesByBusStationId(busStationId)
    }

    suspend fun downloadAllStationsTimetables(normalDirectionLink: String,
                                              reverseDirectionLink: String,
                                              busLineId: Int) {

        val current = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val lastUpdated = formatter.format(current)

        val busStationsNormalDirection = busWebService.getBusStations(normalDirectionLink)
        busStationsNormalDirection.forEach { b ->
            b.direction = "normal"
        }

        val busStationsReverseDirection = busWebService.getBusStations(reverseDirectionLink)
        busStationsReverseDirection.forEach { b ->
            b.direction = "reverse"
        }

        val busStations = busStationsNormalDirection + busStationsReverseDirection

        insertBusStationsInDatabase(busStations, busLineId, lastUpdated)

        busStations.forEach { b ->
            val busTimetables = busWebService.getBusTimetables(b.scheduleLink)

            insertBusTimetablesInDatabase(busTimetables, b.id, lastUpdated)
        }
    }

    private suspend fun insertBusLinesInDatabase(busLines: List<BusLineModel>,
                                                 lastUpdated: String) {
        busLines.forEach { b ->
            b.lastUpdateDate = lastUpdated
        }

        busLinesDao.clearBusLines()
        busLinesDao.saveBusLines(busLines)
    }

    private suspend fun insertBusStationsInDatabase(busStations: List<BusStationModel>,
                                                    busLineId: Int,
                                                    lastUpdated: String,
                                                    direction: String? = null) {
        busStations.forEach { b ->
            b.busLineId = busLineId
            b.lastUpdateDate = lastUpdated

            when {
                direction != null -> {
                    b.direction = direction
                }
            }
        }

        when (direction) {
            // Used from the [downloadAllStationsTimetables()] function is called
            null -> busStationsDao.clearBusStationsByBusLineId(busLineId)
            else -> busStationsDao.clearBusStationsByBusLineIdAndDirection(busLineId, direction)
        }

        busStationsDao.saveBusStations(busStations)
    }

    private suspend fun insertBusTimetablesInDatabase(busTimetables: List<BusTimetableModel>,
                                                      busStationId: Int,
                                                      lastUpdated: String) {
        busTimetables.forEach { b ->
            b.busStationId = busStationId
            b.lastUpdateDate = lastUpdated
        }

        busTimetablesDao.clearBusTimetablesByBusStationId(busStationId)
        busTimetablesDao.saveBusTimetables(busTimetables)
    }
}