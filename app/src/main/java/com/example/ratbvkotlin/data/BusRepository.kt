package com.example.ratbvkotlin.data

import com.example.ratbvkotlin.data.dtos.BusLineDto
import com.example.ratbvkotlin.data.dtos.BusStationDto
import com.example.ratbvkotlin.data.interfaces.IBusWebservice
import com.example.ratbvkotlin.data.models.BusLineModel
import com.example.ratbvkotlin.data.models.BusStationModel
import com.example.ratbvkotlin.data.models.BusTimetableModel
import com.example.ratbvkotlin.data.persistency.BusLinesDataSource
import com.example.ratbvkotlin.data.persistency.BusStationsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import ratbv.BusLineEntity
import ratbv.BusStationEntity
import java.text.SimpleDateFormat
import java.util.*

class BusRepository(
    // TODO Maybe I should send the whole database class here instead of individual Data Sources
    private val _busLinesDataSource: BusLinesDataSource,
    private val _busStationsDataSource: BusStationsDataSource,
    private val _busStationsDao: BusStationsDao,
    private val _busTimetablesDao: BusTimetablesDao,
    private val _busWebService: IBusWebservice
) {

    fun observeBusLines(): Flow<List<BusLineModel>> {
        return _busLinesDataSource.getBusLines()
            .map { busLineEntities ->
                busLineEntities
                    .map { busLineEntity ->
                        BusLineModel(
                            busLineEntity.id,
                            busLineEntity.name,
                            busLineEntity.route,
                            busLineEntity.type,
                            busLineEntity.linkNormalWay,
                            busLineEntity.linkReverseWay,
                            busLineEntity.lastUpdateDate
                        )
                    }
            }
    }

    suspend fun refreshBusLines(
        isForcedRefresh: Boolean
    ) {

        if (!isForcedRefresh &&
            _busLinesDataSource
                .countBusLines() > 0L) {
            return
        }

        val busLines = _busWebService.getBusLines()

        val lastUpdated = getCurrentDateAsString()

        insertBusLinesInDatabase(busLines, lastUpdated)
    }

    fun observeBusStations(
        busLineId: Long
    ): Flow<List<BusStationModel>> {

        return _busStationsDataSource.getBusStationsByBusLineId(busLineId)
            .map { busStationEntities ->
                busStationEntities
                    .map { busStationEntity ->
                        BusStationModel(
                            busStationEntity.id,
                            busStationEntity.busLineId,
                            busStationEntity.name,
                            busStationEntity.direction,
                            busStationEntity.scheduleLink,
                            busStationEntity.lastUpdateDate
                        )
                    }
            }
    }

    suspend fun refreshBusStations(
        busLineId: Long,
        normalDirectionLink: String,
        reverseDirectionLink: String,
        isForcedRefresh: Boolean
    ) {

        if (!isForcedRefresh &&
            _busStationsDataSource
                .countBusStationsByBusLineId(busLineId) > 0L) {
            return
        }

        val busStationsNormalDirection = _busWebService.getBusStations(normalDirectionLink)
        val busStationsReverseDirection = _busWebService.getBusStations(reverseDirectionLink)
        val busStations = busStationsNormalDirection + busStationsReverseDirection

        val lastUpdated = getCurrentDateAsString()

        insertBusStationsInDatabase(busStations, busLineId, lastUpdated)
    }

    suspend fun getBusTimetables(scheduleLink: String,
                                 busStationId: Int,
                                 isForcedRefresh: Boolean)
            : List<BusTimetableModel> {

        val busTimetablesCount = _busTimetablesDao.countBusTimetablesByBusStationId(busStationId)

        if (isForcedRefresh || busTimetablesCount == 0) {

            val busTimetables = _busWebService.getBusTimetables(scheduleLink)

            val lastUpdated = getCurrentDateAsString()

            insertBusTimetablesInDatabase(busTimetables, busStationId, lastUpdated)
        }

        return _busTimetablesDao.getBusTimetablesByBusStationId(busStationId)
    }

    suspend fun downloadAllStationsTimetables(normalDirectionLink: String,
                                              reverseDirectionLink: String,
                                              busLineId: Long) {

        val lastUpdated = getCurrentDateAsString()

        val busStationsNormalDirection = _busWebService.getBusStations(normalDirectionLink)
        val busStationsReverseDirection = _busWebService.getBusStations(reverseDirectionLink)

        val busStations = busStationsNormalDirection + busStationsReverseDirection

        insertBusStationsInDatabase(busStations, busLineId, lastUpdated)

        val insertedBusStations =
            _busStationsDataSource.getBusStationsByBusLineId(busLineId)
                .last()

        insertedBusStations.forEach { busStationModel ->
            val busTimetables = _busWebService.getBusTimetables(busStationModel.scheduleLink)

            insertBusTimetablesInDatabase(busTimetables, busStationModel.id.toInt(), lastUpdated)
        }
    }

    private suspend fun insertBusLinesInDatabase(busLineDtos: List<BusLineDto>,
                                                 lastUpdated: String) {

        val busLines = busLineDtos.map { busLineDto ->
            // TODO change API to replace midibus with electric bus
            val busLineType = when (busLineDto.type) {
                "Midibus" -> "Electricbus"
                else -> busLineDto.type
            }

            BusLineEntity(
                busLineDto.id.toLong(),
                busLineDto.name,
                busLineDto.route,
                busLineType,
                busLineDto.linkNormalWay,
                busLineDto.linkReverseWay,
                lastUpdated
            )
        }

        _busLinesDataSource.clearBusLines()
        _busLinesDataSource.saveBusLines(busLines)
    }

    private suspend fun insertBusStationsInDatabase(
        busStationDtos: List<BusStationDto>,
        busLineId: Long,
        lastUpdated: String
    ) {
        val busStations =  busStationDtos.map { busStationDto ->
            BusStationEntity(
                id = -1,
                busLineId,
                busStationDto.name,
                busStationDto.direction,
                busStationDto.scheduleLink,
                lastUpdated
            )
        }

        _busStationsDataSource.clearBusStationsByBusLineId(busLineId)
        _busStationsDataSource.saveBusStations(busStations)
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

    private fun getCurrentDateAsString() : String {
        val current = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")

        return formatter.format(current)
    }
}