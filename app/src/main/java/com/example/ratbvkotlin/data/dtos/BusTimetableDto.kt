package com.example.ratbvkotlin.data.dtos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BusTimetableDto (

    @Expose
    @SerializedName("hour")
    val hour: String,

    @Expose
    @SerializedName("minutes")
    val minutes: String,

    @Expose
    @SerializedName("timeOfWeek")
    val timeOfWeek: String,
)