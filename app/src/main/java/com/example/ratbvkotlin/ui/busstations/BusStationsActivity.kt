package com.example.ratbvkotlin.ui.busstations

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.example.ratbvkotlin.R

class BusStationsActivity : AppCompatActivity(R.layout.activity_bus_stations) {

    private val args: BusStationsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = findNavController(R.id.bus_station_list_nav_host_fragment)
        navController.setGraph(R.navigation.bus_stations_navigation, args.toBundle())

        setupToolbar()
    }

    /**
     * Forces [BusStationsActivity] appbar to display the back button in the appbar
     */
    private fun setupToolbar() {

        // Sets the title of the action bar based on the bus line
        //supportActionBar?.title = "this is a test "

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    /**
     * Finishes the current [BusStationsActivity] when pressing the appbar back button
     */
    override fun onSupportNavigateUp() : Boolean {
        finish()
        return true
    }
}
