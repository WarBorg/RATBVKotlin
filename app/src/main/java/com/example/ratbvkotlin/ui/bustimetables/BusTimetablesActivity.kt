package com.example.ratbvkotlin.ui.bustimetables

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.navArgs
import com.example.ratbvkotlin.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BusTimetablesActivity : AppCompatActivity(R.layout.activity_bus_timetables) {

    private val args: BusTimetablesActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val navView: BottomNavigationView = findViewById(R.id.bus_timetable_list_nav_view)

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

    /**
     * Forces [BusTimetablesActivity] appbar to display the back button in the appbar
     */
    private fun setupSupportActionBar() {

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    /**
     * Replaces timetable fragments based on type of week
     */
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

    /**
     * Finishes the current [BusTimetablesActivity] when pressing the appbar back button
     */
    override fun onSupportNavigateUp() : Boolean {
        finish()
        return true
    }
}
