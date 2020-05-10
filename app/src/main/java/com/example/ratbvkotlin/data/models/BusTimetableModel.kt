package com.example.ratbvkotlin.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "BusTimetables",
        foreignKeys = [ForeignKey(entity = BusLineModel::class,
                                  parentColumns = ["Id"],
                                  childColumns = ["BusStationId"],
                                  onDelete = ForeignKey.CASCADE)]
)
data class BusTimetableModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Int,
    @ColumnInfo(name = "BusStationId")
    val busStationId: Int,
    @ColumnInfo(name = "Hour")
    val hour: String,
    @ColumnInfo(name = "Minutes")
    val minutes: String,
    @ColumnInfo(name = "TimeOfWeek")
    val timeOfWeek: String,
    @ColumnInfo(name = "LastUpdateDate")
    val lastUpdateDate: String
)