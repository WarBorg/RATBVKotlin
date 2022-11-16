package com.example.ratbvkotlin.data

import com.example.ratbvkotlin.data.dtos.BusLineDto
import com.example.ratbvkotlin.data.dtos.BusStationDto
import com.example.ratbvkotlin.data.dtos.BusTimetableDto
import com.example.ratbvkotlin.data.interfaces.IBusWebservice
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Retrofit instance which holds details about the API calls.
 */
interface BusApi {

    /**
     * Resulting URL: https://ratbvwebapi.azurewebsites.net/api/buslines
     */
    @GET("buslines")
    suspend fun getBusLines(): List<BusLineDto>

    /**
     * Resulting URL: https://ratbvwebapi.azurewebsites.net/api/busstations/afisaje___5-dus.html
     */
    @GET("busstations/{lineNumberLink}")
    suspend fun getBusStations(
        @Path("lineNumberLink") lineNumberLink: String
    ): List<BusStationDto>

    /**
     * Resulting URL: https://ratbvwebapi.azurewebsites.net/api/bustimetables/5-intors___line_5_11_cl1_ro.html
     */
    @GET("bustimetables/{scheduleLink}")
    suspend fun getBusTimetables(
        @Path("scheduleLink") scheduleLink: String
    ): List<BusTimetableDto>
}

/**
 * Class responsible with initialisation of the [BusApi] Retrofit instance
 * and forwarding API calls to it.
 */
class BusWebService : IBusWebservice {

    private val api: BusApi by lazy {

        val ktorClient = HttpClient() {
            install(ContentNegotiation) {
                json(Json { isLenient = true; ignoreUnknownKeys = true })
            }
        }

        Ktorfit.Builder()
            .baseUrl(BASE_URL)
            .httpClient(ktorClient)
            .build()
            .create<BusApi>()
    }

    override suspend fun getBusLines(): List<BusLineDto> = api.getBusLines()
    override suspend fun getBusStations(lineNumberLink: String): List<BusStationDto> = api.getBusStations(lineNumberLink)
    override suspend fun getBusTimetables(scheduleLink: String): List<BusTimetableDto> = api.getBusTimetables(scheduleLink)

    companion object {
        const val BASE_URL = "https://ratbvwebapi.azurewebsites.net/api/"
    }
}