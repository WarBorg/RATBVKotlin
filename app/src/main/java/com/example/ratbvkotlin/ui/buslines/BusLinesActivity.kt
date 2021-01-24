package com.example.ratbvkotlin.ui.buslines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ratbvkotlin.R

// Define activity class and link it with layout file
class BusLinesActivity : AppCompatActivity(R.layout.activity_bus_lines) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get bottom tab bar and navigation host fragment
        /*val navView: BottomNavigationView = findViewById(R.id.bus_line_list_nav_view)
        val navController = findNavController(R.id.bus_line_list_nav_host_fragment)

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.bus_dest,
                R.id.midibus_dest,
                R.id.trolleybus_dest
            )
        )

        // Creates correlation between top bar and actual fragment displayed
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Creates correlation between bottom tab bar and actual fragment displayed
        navView.setupWithNavController(navController)*/
    }
}
