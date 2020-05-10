package com.example.ratbvkotlin.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ratbvkotlin.data.models.BusStationModel

@Dao
interface BusStationsDao {

    @Query("SELECT COUNT(*) FROM BusStations WHERE busLineId = :busLineId AND direction = :direction")
    suspend fun countBusStationsByBusLineIdAndDirection(busLineId: Int, direction: String): Int

    @Query("SELECT * FROM BusStations WHERE busLineId = :busLineId AND direction = :direction")
    fun getBusStationsByBusLineIdAndDirection(busLineId: Int, direction: String): LiveData<List<BusStationModel>>

    @Insert
    suspend fun saveBusStations(list: List<BusStationModel>)

    @Query("DELETE FROM BusStations")
    suspend fun clearBusLines()
}