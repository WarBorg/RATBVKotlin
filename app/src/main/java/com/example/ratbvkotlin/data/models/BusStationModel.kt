package com.example.ratbvkotlin.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ForeignKey.CASCADE


@Entity(tableName = "BusStations",
        foreignKeys = [ForeignKey(entity = BusLineModel::class,
                                  parentColumns = ["Id"],
                                  childColumns = ["BusLineId"],
                                  onDelete = CASCADE)]
)
data class BusStationModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Int,
    @ColumnInfo(name = "BusLineId")
    val busLineId: Int,
    @ColumnInfo(name = "Name")
    val name: String,
    @ColumnInfo(name = "Direction")
    val direction: String,
    @ColumnInfo(name = "ScheduleLink")
    val scheduleLink: String,
    @ColumnInfo(name = "LastUpdateDate")
    val lastUpdateDate: String
)