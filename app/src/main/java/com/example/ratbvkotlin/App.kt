package com.example.ratbvkotlin

import android.app.Application
import androidx.room.Room
import com.example.ratbvkotlin.data.BusDatabase
import com.example.ratbvkotlin.data.BusDatabase.Companion.DATABASE_NAME
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.BusWebService
import com.example.ratbvkotlin.data.interfaces.IBusWebservice
import com.example.ratbvkotlin.ui.buslines.BusLinesViewModel
import com.example.ratbvkotlin.ui.busstations.BusStationsViewModel
import com.example.ratbvkotlin.ui.bustimetables.BusTimetablesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize dependency injection
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

    // Module containing all project dependencies
    private val appModule = module {
        single { Room.databaseBuilder(get(), BusDatabase::class.java, DATABASE_NAME).build() }
        single { get<BusDatabase>().busLinesDao() }
        single { get<BusDatabase>().busStationsDao() }
        single { get<BusDatabase>().busTimetablesDao() }
        single { BusWebService() as IBusWebservice }
        /*single { BusWebServiceMock() as IBusWebservice }*/
        single { BusRepository(get(), get(), get() , get()) }
        viewModel { (busTransportSubtype : String) -> BusLinesViewModel(get(), busTransportSubtype) }
        viewModel {
                (directionLink: String, direction: String, busLineId : Int) ->
            BusStationsViewModel(get(), directionLink, direction, busLineId)
        }
        viewModel {
                (scheduleLink: String, busStationId : Int, timetableType: String) ->
            BusTimetablesViewModel(get(), scheduleLink, busStationId, timetableType)
        }
    }
}