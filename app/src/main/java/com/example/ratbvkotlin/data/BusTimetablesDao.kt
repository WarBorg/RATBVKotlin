package com.example.ratbvkotlin.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ratbvkotlin.data.models.BusTimetableModel

@Dao
interface BusTimetablesDao {

    @Query("SELECT COUNT(*) FROM BusTimetables WHERE busStationId = :busStationId")
    suspend fun countBusTimetablesByBusStationId(busStationId: Int): Int

    @Query("SELECT * FROM BusTimetables WHERE busStationId = :busStationId")
    fun getBusTimetablesByBusStationId(busStationId: Int): LiveData<List<BusTimetableModel>>

    @Insert
    suspend fun saveBusStations(list: List<BusTimetableModel>)

    @Query("DELETE FROM BusTimetables")
    suspend fun clearBusTimetables()
}