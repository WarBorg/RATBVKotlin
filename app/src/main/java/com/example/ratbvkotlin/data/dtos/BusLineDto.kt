package com.example.ratbvkotlin.data.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BusLineDto(

    @SerialName("id")
    val id: Long,

    @SerialName("name")
    val name: String,

    @SerialName("route")
    val route: String,

    @SerialName("type")
    // TODO change API to replace midibus with electric bus
    var type: String,

    @SerialName("linkNormalWay")
    val linkNormalWay: String,

    @SerialName("linkReverseWay")
    val linkReverseWay: String,
)