package com.example.ratbvkotlin.ui.bustimetables

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.ratbvkotlin.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BusTimetablesActivity : AppCompatActivity() {

    private val args: BusTimetablesFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bus_timetables)

        val navView: BottomNavigationView = findViewById(R.id.bus_timetable_list_nav_view)

        val navController = findNavController(R.id.bus_timetable_list_nav_host_fragment)
        navController.setGraph(R.navigation.bus_timetables_navigation, args.toBundle())

        setupToolbar(navController)

        /** Passing each menu ID as a set of Ids because each
         *  menu should be considered as top level destinations.
         */
        //val appBarConfiguration = AppBarConfiguration(
        //    setOf(
        //        R.id.station_list_dest
        //    )
        //)

        //setupActionBarWithNavController(navController, appBarConfiguration)

        //navView.setupWithNavController(navController)
    }

    /**
     * Forces [BusTimetablesActivity] appbar to display the back button in the appbar
     */
    private fun setupToolbar(navController: NavController) {

        val appBarConfiguration =
            AppBarConfiguration.Builder()
                .setFallbackOnNavigateUpListener { navController.navigateUp() }
                .build()

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    /**
     * Finishes the current [BusTimetablesActivity] when pressing the appbar back button
     */
    override fun onSupportNavigateUp() : Boolean {
        finish()
        return true
    }
}
