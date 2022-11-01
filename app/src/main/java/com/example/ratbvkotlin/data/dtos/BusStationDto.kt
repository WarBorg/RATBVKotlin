package com.example.ratbvkotlin.data.dtos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BusStationDto (

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("direction")
    var direction: String,

    @Expose
    @SerializedName("scheduleLink")
    val scheduleLink: String,
)