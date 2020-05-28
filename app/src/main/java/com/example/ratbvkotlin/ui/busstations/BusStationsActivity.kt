package com.example.ratbvkotlin.ui.busstations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.ratbvkotlin.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BusStationsActivity : AppCompatActivity() {

    private val args: BusStationsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bus_stations)

        val navView: BottomNavigationView = findViewById(R.id.bus_station_list_nav_view)

        val navController = findNavController(R.id.bus_station_list_nav_host_fragment)
        navController.setGraph(R.navigation.bus_stations_navigation, args.toBundle())

        setupToolbar(navController)
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
