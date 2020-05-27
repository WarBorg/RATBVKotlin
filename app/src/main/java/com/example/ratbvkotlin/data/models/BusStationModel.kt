package com.example.ratbvkotlin.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ForeignKey.CASCADE
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Model used to describe the bus station in the DataBase as well as in the Web API
 */
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

    @ColumnInfo(name = "BusLineId", index = true)
    var busLineId: Int,

    @Expose
    @SerializedName("name")
    @ColumnInfo(name = "Name")
    val name: String,

    @Expose
    @SerializedName("direction")
    @ColumnInfo(name = "Direction")
    var direction: String,

    @Expose
    @SerializedName("scheduleLink")
    @ColumnInfo(name = "ScheduleLink")
    val scheduleLink: String,

    @ColumnInfo(name = "LastUpdateDate")
    var lastUpdateDate: String
)