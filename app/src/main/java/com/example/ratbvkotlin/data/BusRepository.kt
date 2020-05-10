package com.example.ratbvkotlin.data

class BusRepository(
    private val busLinesDao: BusLinesDao,
    private val busStationsDao: BusStationsDao,
    private val busTimetablesDao: BusTimetablesDao,
    private val webService: BusWebService) {

}