package com.example.ratbvkotlin.data.models

/**
 * Model used to describe the bus timetable in the [BusRepository]
 */
data class BusTimetableModel(
    val id: Long,
    var busStationId: Long,
    val hour: String,
    val minutes: String,
    val timeOfWeek: String,
    var lastUpdateDate: String
)