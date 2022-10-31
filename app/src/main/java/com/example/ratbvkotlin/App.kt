package com.example.ratbvkotlin

import android.app.Application
import androidx.room.Room
import com.example.ratbvkotlin.data.BusDatabase
import com.example.ratbvkotlin.data.BusDatabase.Companion.DATABASE_NAME
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.BusWebService
import com.example.ratbvkotlin.data.interfaces.IBusWebservice
import com.example.ratbvkotlin.data.persistency.*
import com.example.ratbvkotlin.viewmodels.BusLinesViewModel
import com.example.ratbvkotlin.viewmodels.BusStationsViewModel
import com.example.ratbvkotlin.viewmodels.BusTimetablesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize dependency injection
        startKoin {
            androidContext(this@App)
            modules(
                persistencyModule,
                servicesModule,
                viewModelsModule
            )
        }
    }
    
    private val persistencyModule = module {

        // Room Database dependencies
        single {
            Room.databaseBuilder(
                get(),
                BusDatabase::class.java,
                DATABASE_NAME
            ).build()
        }

        single {
            get<BusDatabase>().busStationsDao()
        }
        single {
            get<BusDatabase>().busTimetablesDao()
        }

        // SQLDelight Database dependencies
        single<BusLinesDataSource> {
            BusLinesDataSourceImpl(DatabaseDriverFactory(androidContext()))
        }

        single<BusStationsDataSource> {
            BusStationsDataSourceImpl(DatabaseDriverFactory(androidContext()))
        }
    }

    private val servicesModule = module {
        // Web service dependencies
        single {
            BusWebService() as IBusWebservice
        }
        /*single { BusWebServiceMock() as IBusWebservice }*/

        // Repository dependencies
        single {
            BusRepository(
                get(),
                get(),
                get(),
                get()
            )
        }
    }
    
    private val viewModelsModule = module {
        // View Model dependencies
        viewModel {
            BusLinesViewModel(get())
        }
        viewModel { (
                    directionLinkNormal: String,
                    directionLinkReverse: String,
                    busLineId: Int, busLineName: String
                ) ->
            BusStationsViewModel(
                get(),
                directionLinkNormal,
                directionLinkReverse,
                busLineId,
                busLineName
            )
        }
        viewModel { (
                    scheduleLink: String,
                    busStationId: Int,
                    busStationName: String
                ) ->
            BusTimetablesViewModel(
                get(),
                scheduleLink,
                busStationId,
                busStationName
            )
        }
    }
}