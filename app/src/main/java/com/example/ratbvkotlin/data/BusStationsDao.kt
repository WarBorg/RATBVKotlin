package com.example.ratbvkotlin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ratbvkotlin.data.models.BusStationModel

@Dao
interface BusStationsDao {

    @Query("SELECT COUNT(*) FROM BusStations WHERE busLineId = :busLineId AND direction = :direction")
    suspend fun countBusStationsByBusLineIdAndDirection(busLineId: Int, direction: String): Int

    @Query("SELECT * FROM BusStations WHERE busLineId = :busLineId")
    suspend fun getBusStationsByBusLineId(busLineId: Int): List<BusStationModel>

    @Query("SELECT * FROM BusStations WHERE busLineId = :busLineId AND direction = :direction")
    suspend fun getBusStationsByBusLineIdAndDirection(busLineId: Int, direction: String): List<BusStationModel>

    @Insert
    suspend fun saveBusStations(list: List<BusStationModel>)

    @Query("DELETE FROM BusStations WHERE busLineId = :busLineId")
    suspend fun clearBusStationsByBusLineId(busLineId: Int)

    @Query("DELETE FROM BusStations WHERE busLineId = :busLineId AND direction = :direction")
    suspend fun clearBusStationsByBusLineIdAndDirection(busLineId: Int, direction: String)
}