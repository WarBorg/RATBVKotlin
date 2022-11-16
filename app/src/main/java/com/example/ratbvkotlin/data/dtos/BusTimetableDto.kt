package com.example.ratbvkotlin.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BusTimetableDto (

    @SerialName("hour")
    val hour: String,

    @SerialName("minutes")
    val minutes: String,

    @SerialName("timeOfWeek")
    val timeOfWeek: String,
)