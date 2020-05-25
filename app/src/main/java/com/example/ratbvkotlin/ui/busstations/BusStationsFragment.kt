package com.example.ratbvkotlin.ui.busstations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.ratbvkotlin.databinding.FragmentBusStationListBinding
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Shows the bus stations for a specific bus line
 * List layout [fragment_bus_station_list.xml]
 * List item layout [fragment_bus_station.xml]
 */
class BusStationsFragment : Fragment() {

    private lateinit var binding: FragmentBusStationListBinding

    // Gets the arguments passed to the fragment
    private val args: BusStationsFragmentArgs by navArgs()
    // Sets the viewmodel parameters with the necessary arguments
    private val busStationsViewModel: BusStationsViewModel by viewModel { parametersOf(args.busLineId) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Links the binding to the fragment layout [fragment_bus_station_list.xml]
        binding = FragmentBusStationListBinding.inflate(layoutInflater)

        val busLineId = args.busLineId
        binding.stationTestText.text = "Hello from line ${busLineId}"

        return binding.root
    }
}
