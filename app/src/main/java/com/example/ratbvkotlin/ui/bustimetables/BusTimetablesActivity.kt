package com.example.ratbvkotlin.ui.bustimetables

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.fragment.app.commit
import androidx.navigation.navArgs
import com.example.ratbvkotlin.R
import com.google.android.material.bottomnavigation.BottomNavigationView
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
            BusTimetablesScreen(busTimetablesViewModel)
        }
    }
}
        /*val navView: BottomNavigationView = findViewById(R.id.bus_timetable_list_nav_view)

        setupSupportActionBar()

        // Creates the listener for switching the bottom navigation tabs
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.weekdays_dest -> replaceTimetableFragment("WeekDays")
                R.id.saturday_dest -> replaceTimetableFragment("Saturday")
                R.id.sunday_dest -> replaceTimetableFragment("Sunday")
            }

            true
        }

        // Loads the first tab fragment when Activity is created
        replaceTimetableFragment("WeekDays")
    }


    private fun setupSupportActionBar() {

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }


    private fun replaceTimetableFragment(timetableType: String) {

        val timetablesFragment = BusTimetablesFragment.newInstance(
            args.scheduleLink,
            args.busStationId,
            args.busStationName,
            timetableType
        )

        supportFragmentManager.commit {
            replace(R.id.bus_timetable_list_host_fragment, timetablesFragment)
        }
    }


    override fun onSupportNavigateUp() : Boolean {
        finish()
        return true
    }
*/
