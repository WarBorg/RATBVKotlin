package com.example.ratbvkotlin.ui.busstations

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ratbvkotlin.R

class BusStationsActivity : AppCompatActivity() {

    private val args: BusStationsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bus_stations)

        val navView: BottomNavigationView = findViewById(R.id.bus_station_list_nav_view)

        val navController = findNavController(R.id.bus_station_list_nav_host_fragment)
        navController.setGraph(R.navigation.bus_stations_navigation, args.toBundle())

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
     * Forces [BusStationsActivity] appbar to display the back button in the appbar
     */
    private fun setupToolbar(navController: NavController) {

        val appBarConfiguration =
            AppBarConfiguration.Builder()
                .setFallbackOnNavigateUpListener { navController.navigateUp() }
                .build()

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    /**
     * Finishes the current [BusStationsActivity] when pressing the appbar back button
     */
    override fun onSupportNavigateUp() : Boolean {
        finish()
        return true
    }
}
