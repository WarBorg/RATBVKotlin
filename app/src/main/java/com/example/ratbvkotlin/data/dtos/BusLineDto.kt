package com.example.ratbvkotlin.data.dtos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BusLineDto(

    @Expose
    @SerializedName("id")
    val id: Int,

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("route")
    val route: String,

    @Expose
    @SerializedName("type")
    // TODO change API to replace midibus with electric bus
    var type: String,

    @Expose
    @SerializedName("linkNormalWay")
    val linkNormalWay: String,

    @Expose
    @SerializedName("linkReverseWay")
    val linkReverseWay: String,
)