package com.example.ratbvkotlin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ratbvkotlin.data.models.BusTimetableModel

@Dao
interface BusTimetablesDao {

    @Query("SELECT COUNT(*) FROM BusTimetables WHERE busStationId = :busStationId")
    suspend fun countBusTimetablesByBusStationId(busStationId: Int): Int

    @Query("SELECT * FROM BusTimetables WHERE busStationId = :busStationId")
    suspend fun getBusTimetablesByBusStationId(busStationId: Int): List<BusTimetableModel>

    @Insert
    suspend fun saveBusTimetables(list: List<BusTimetableModel>)

    @Query("DELETE FROM BusTimetables WHERE busStationId = :busStationId")
    suspend fun clearBusTimetablesByBusStationId(busStationId: Int)
}