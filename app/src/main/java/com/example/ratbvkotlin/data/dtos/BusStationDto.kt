package com.example.ratbvkotlin.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BusStationDto (

    @SerialName("name")
    val name: String,

    @SerialName("direction")
    var direction: String,

    @SerialName("scheduleLink")
    val scheduleLink: String,
)