package com.example.ratbvkotlin.data.models

/**
 * Model used to describe the bus station in the [BusRepository]
 */
data class BusStationModel(
    val id: Long,
    var busLineId: Long,
    val name: String,
    var direction: String,
    val scheduleLink: String,
    var lastUpdateDate: String
)