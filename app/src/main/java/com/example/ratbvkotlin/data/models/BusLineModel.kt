package com.example.ratbvkotlin.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

//region API models
/*data class Forecast(
    @SerializedName("list")
    val days: List<Day>
)

data class Day(
    @SerializedName("dt")
    val date: Date,
    @SerializedName("main")
    val mainInfo: MainInfo,
    @SerializedName("weather")
    val weatherInfo: List<WeatherInfo>,
    @SerializedName("wind")
    val windInfo: WindInfo
)

data class MainInfo(
    @SerializedName("feels_like")
    val feelsLike: Float,
    @SerializedName("humidity")
    val humidity: Float,
    @SerializedName("pressure")
    val pressure: Float,
    @SerializedName("temp")
    val temp: Float,
    @SerializedName("temp_max")
    val tempMax: Float,
    @SerializedName("temp_min")
    val tempMin: Float
)

data class WeatherInfo(
    @SerializedName("description")
    val description: String,
    @SerializedName("main")
    val main: String
)

data class WindInfo(
    @SerializedName("deg")
    val direction: String,
    @SerializedName("speed")
    val speed: Float
)*/
//endregion


// Business logic models
/**
 * Model used to describe the bus line
 */
@Entity(tableName = "BusLines")
data class BusLineModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Int,
    @ColumnInfo(name = "Name")
    val name: String,
    @ColumnInfo(name = "Route")
    val route: String,
    @ColumnInfo(name = "Type")
    val type: String,
    @ColumnInfo(name = "LinkNormalWay")
    val linkNormalWay: String,
    @ColumnInfo(name = "LinkReverseWay")
    val linkReverseWay: String,
    @ColumnInfo(name = "LastUpdateDate")
    val lastUpdateDate: String
)

//fun Day.toDayForecast(): DayForecast =
//    DayForecast(
//        date = date,
//        feelsLike = mainInfo.feelsLike,
//        humidity = mainInfo.humidity,
//        pressure = mainInfo.pressure,
//        temp = mainInfo.temp,
//        tempMax = mainInfo.tempMax,
//        tempMin = mainInfo.tempMin,
//        weatherDescription = weatherInfo[0].description,
//        weatherShort = weatherInfo[0].main,
//        windDirection = windInfo.direction,
//        windSpeed = windInfo.speed
//    )
//endregion