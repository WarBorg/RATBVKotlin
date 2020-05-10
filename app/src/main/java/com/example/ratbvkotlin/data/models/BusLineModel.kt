package com.example.ratbvkotlin.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Model used to describe the bus line in the DataBase as well as in the Web API
 */
@Entity(tableName = "BusLines")
data class BusLineModel(

    @Expose
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Int,

    @Expose
    @SerializedName("name")
    @ColumnInfo(name = "Name")
    val name: String,

    @Expose
    @SerializedName("route")
    @ColumnInfo(name = "Route")
    val route: String,

    @Expose
    @SerializedName("type")
    @ColumnInfo(name = "Type")
    val type: String,

    @Expose
    @SerializedName("linkNormalWay")
    @ColumnInfo(name = "LinkNormalWay")
    val linkNormalWay: String,

    @Expose
    @SerializedName("linkReverseWay")
    @ColumnInfo(name = "LinkReverseWay")
    val linkReverseWay: String,

    @ColumnInfo(name = "LastUpdateDate")
    var lastUpdateDate: String
)