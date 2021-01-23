package com.example.ratbvkotlin.ui.busstations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.example.ratbvkotlin.R

// TODO Remove when adding composable BusLines
class BusStationsActivity : AppCompatActivity(R.layout.activity_bus_stations) {

    private val args: BusStationsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = findNavController(R.id.bus_station_list_nav_host_fragment)
        navController.setGraph(R.navigation.bus_stations_navigation, args.toBundle())
    }
}
