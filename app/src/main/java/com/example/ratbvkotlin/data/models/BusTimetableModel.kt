package com.example.ratbvkotlin.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Model used to describe the bus timetable in the DataBase as well as in the Web API
 */
@Entity(tableName = "BusTimetables",
        foreignKeys = [ForeignKey(entity = BusStationModel::class,
                                  parentColumns = ["Id"],
                                  childColumns = ["BusStationId"],
                                  onDelete = ForeignKey.CASCADE)]
)
data class BusTimetableModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Long,

    @ColumnInfo(name = "BusStationId", index = true)
    var busStationId: Long,

    @Expose
    @SerializedName("hour")
    @ColumnInfo(name = "Hour")
    val hour: String,

    @Expose
    @SerializedName("minutes")
    @ColumnInfo(name = "Minutes")
    val minutes: String,

    @Expose
    @SerializedName("timeOfWeek")
    @ColumnInfo(name = "TimeOfWeek")
    val timeOfWeek: String,

    @ColumnInfo(name = "LastUpdateDate")
    var lastUpdateDate: String
)