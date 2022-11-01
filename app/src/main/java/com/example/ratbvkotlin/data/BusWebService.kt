package com.example.ratbvkotlin.data

import com.example.ratbvkotlin.data.dtos.BusLineDto
import com.example.ratbvkotlin.data.dtos.BusStationDto
import com.example.ratbvkotlin.data.dtos.BusTimetableDto
import com.example.ratbvkotlin.data.interfaces.IBusWebservice
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

/**
 * Class responsible with initialisation of the [BusApi] Retrofit instance
 * and forwarding API calls to it.
 */
class BusWebService : IBusWebservice {

    private val api: BusApi by lazy {
        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                                .create()

        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(1, TimeUnit.MINUTES)
            .callTimeout(1, TimeUnit.MINUTES)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(BusApi::class.java)
    }

    override suspend fun getBusLines(): List<BusLineDto> = api.getBusLines()
    override suspend fun getBusStations(lineNumberLink: String): List<BusStationDto> = api.getBusStations(lineNumberLink)
    override suspend fun getBusTimetables(scheduleLink: String): List<BusTimetableDto> = api.getBusTimetables(scheduleLink)

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

    companion object {
        const val BASE_URL = "https://ratbvwebapi.azurewebsites.net/api/"
    }
}