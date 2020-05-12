package com.example.ratbvkotlin.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ratbvkotlin.data.models.BusLineModel

@Dao
interface BusLinesDao {

    @Query("SELECT COUNT(*) FROM BusLines")
    suspend fun countBusLines(): Int

    @Query("SELECT * FROM BusLines")
    suspend fun getBusLines(): List<BusLineModel>

    @Insert
    suspend fun saveBusLines(list: List<BusLineModel>)

    @Query("DELETE FROM BusLines")
    suspend fun clearBusLines()
}