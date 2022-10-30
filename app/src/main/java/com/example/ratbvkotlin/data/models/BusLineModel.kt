package com.example.ratbvkotlin.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Model used to describe the bus line in the DataBase
 */
@Entity(tableName = "BusLines")
data class BusLineModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Int,

    @ColumnInfo(name = "Name")
    val name: String,

    @ColumnInfo(name = "Route")
    val route: String,

    @ColumnInfo(name = "Type")
    // TODO change API to replace midibus with electric bus
    var type: String,

    @ColumnInfo(name = "LinkNormalWay")
    val linkNormalWay: String,

    @ColumnInfo(name = "LinkReverseWay")
    val linkReverseWay: String,

    @ColumnInfo(name = "LastUpdateDate")
    var lastUpdateDate: String
)