package com.example.ratbvkotlin.ui.bustimetables

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.navigation.navArgs
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

//class BusTimetablesActivity : AppCompatActivity(R.layout.activity_bus_timetables) {
class BusTimetablesActivity : AppCompatActivity() {

    private val args: BusTimetablesActivityArgs by navArgs()

    // Sets the viewmodel parameters with the necessary arguments
    private val busTimetablesViewModel: BusTimetablesViewModel by viewModel {
        parametersOf(args.scheduleLink, args.busStationId, args.busStationName, "WeekDays")
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            BusTimetablesScaffoldScreen(busTimetablesViewModel,
                                        onBackNavigation = { finish() })
        }
    }
}