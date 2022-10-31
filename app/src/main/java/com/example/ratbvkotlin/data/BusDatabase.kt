package com.example.ratbvkotlin.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ratbvkotlin.data.models.BusLineModel
import com.example.ratbvkotlin.data.models.BusStationModel
import com.example.ratbvkotlin.data.models.BusTimetableModel

@Database(entities = [BusStationModel::class, BusTimetableModel::class],
          version = 1,
          exportSchema = false)
abstract class BusDatabase : RoomDatabase() {

    abstract fun busStationsDao(): BusStationsDao?
    abstract fun busTimetablesDao(): BusTimetablesDao?

    companion object {
        const val DATABASE_NAME: String = "ratbv.db"
    }
}