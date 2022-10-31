package com.example.ratbvkotlin.data.models

/**
 * Model used to describe the bus line in the [BusRepository]
 */
data class BusLineModel(
    val id: Long,
    val name: String,
    val route: String,
    // TODO change API to replace midibus with electric bus
    var type: String,
    val linkNormalWay: String,
    val linkReverseWay: String,
    var lastUpdateDate: String
)