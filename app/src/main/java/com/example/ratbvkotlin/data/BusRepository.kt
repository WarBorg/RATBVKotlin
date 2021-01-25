package com.example.ratbvkotlin.data

import com.example.ratbvkotlin.data.interfaces.IBusWebservice
import com.example.ratbvkotlin.data.models.BusLineModel
import com.example.ratbvkotlin.data.models.BusStationModel
import com.example.ratbvkotlin.data.models.BusTimetableModel
import java.text.SimpleDateFormat
import java.util.*

class BusRepository(
    // TODO Maybe I should send the whole database class here instead of individual DAO's
    private val _busLinesDao: BusLinesDao,
    private val _busStationsDao: BusStationsDao,
    private val _busTimetablesDao: BusTimetablesDao,
    private val _busWebService: IBusWebservice
) {

    suspend fun getBusLines(isForcedRefresh: Boolean): List<BusLineModel> {

        val busLinesCount = _busLinesDao.countBusLines()

        if (isForcedRefresh || busLinesCount == 0) {

            val busLines = _busWebService.getBusLines()
            val current = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val lastUpdated = formatter.format(current)

            insertBusLinesInDatabase(busLines, lastUpdated)
        }

        return _busLinesDao.getBusLines()
    }

    suspend fun getBusStations(directionLink: String,
                               direction: String,
                               busLineId: Int,
                               isForcedRefresh: Boolean)
            : List<BusStationModel> {

        val busStationsCount = _busStationsDao.countBusStationsByBusLineIdAndDirection(busLineId, direction)

        if (isForcedRefresh || busStationsCount == 0) {

            val busStations = _busWebService.getBusStations(directionLink)
            val current = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val lastUpdated = formatter.format(current)

            insertBusStationsInDatabase(busStations, busLineId, lastUpdated, direction)
        }

        return _busStationsDao.getBusStationsByBusLineIdAndDirection(busLineId, direction)
    }

    suspend fun getBusTimetables(scheduleLink: String,
                                 busStationId: Int,
                                 isForcedRefresh: Boolean)
            : List<BusTimetableModel> {

        val busTimetablesCount = _busTimetablesDao.countBusTimetablesByBusStationId(busStationId)

        if (isForcedRefresh || busTimetablesCount == 0) {

            val busTimetables = _busWebService.getBusTimetables(scheduleLink)
            val current = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val lastUpdated = formatter.format(current)

            insertBusTimetablesInDatabase(busTimetables, busStationId, lastUpdated)
        }

        return _busTimetablesDao.getBusTimetablesByBusStationId(busStationId)
    }

    suspend fun downloadAllStationsTimetables(normalDirectionLink: String,
                                              reverseDirectionLink: String,
                                              busLineId: Int) {

        val current = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val lastUpdated = formatter.format(current)

        val busStationsNormalDirection = _busWebService.getBusStations(normalDirectionLink)
        busStationsNormalDirection.forEach { busStationModel ->
            busStationModel.direction = "normal"
        }

        val busStationsReverseDirection = _busWebService.getBusStations(reverseDirectionLink)
        busStationsReverseDirection.forEach { busStationModel ->
            busStationModel.direction = "reverse"
        }

        val busStations = busStationsNormalDirection + busStationsReverseDirection

        insertBusStationsInDatabase(busStations, busLineId, lastUpdated)

        val insertedBusStations = _busStationsDao.getBusStationsByBusLineId(busLineId)

        insertedBusStations.forEach { busStationModel ->
            val busTimetables = _busWebService.getBusTimetables(busStationModel.scheduleLink)

            insertBusTimetablesInDatabase(busTimetables, busStationModel.id, lastUpdated)
        }
    }

    private suspend fun insertBusLinesInDatabase(busLines: List<BusLineModel>,
                                                 lastUpdated: String) {
        busLines.forEach { busLineModel ->
            busLineModel.lastUpdateDate = lastUpdated
            // TODO change API to replace midibus with electric bus
            busLineModel.type = when (busLineModel.type) {
                "Midibus" -> "Electricbus"
                else -> busLineModel.type
            }
        }

        _busLinesDao.clearBusLines()
        _busLinesDao.saveBusLines(busLines)
    }

    private suspend fun insertBusStationsInDatabase(busStations: List<BusStationModel>,
                                                    busLineId: Int,
                                                    lastUpdated: String,
                                                    direction: String? = null): List<BusStationModel> {
        busStations.forEach { busStationModel ->
            busStationModel.busLineId = busLineId
            busStationModel.lastUpdateDate = lastUpdated

            when {
                direction != null -> {
                    busStationModel.direction = direction
                }
            }
        }

        when (direction) {
            // Used from the [downloadAllStationsTimetables()] function is called
            null -> _busStationsDao.clearBusStationsByBusLineId(busLineId)
            else -> _busStationsDao.clearBusStationsByBusLineIdAndDirection(busLineId, direction)
        }

        _busStationsDao.saveBusStations(busStations)

        return busStations
    }

    private suspend fun insertBusTimetablesInDatabase(busTimetables: List<BusTimetableModel>,
                                                      busStationId: Int,
                                                      lastUpdated: String) {
        busTimetables.forEach { busTimetableModel ->
            busTimetableModel.busStationId = busStationId
            busTimetableModel.lastUpdateDate = lastUpdated
        }

        _busTimetablesDao.clearBusTimetablesByBusStationId(busStationId)
        _busTimetablesDao.saveBusTimetables(busTimetables)
    }
}