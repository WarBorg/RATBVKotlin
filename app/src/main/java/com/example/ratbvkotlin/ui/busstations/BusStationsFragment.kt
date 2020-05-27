package com.example.ratbvkotlin.ui.busstations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.ratbvkotlin.databinding.FragmentBusStationListBinding
import kotlinx.coroutines.launch
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
    private val busStationsViewModel: BusStationsViewModel by viewModel {
        parametersOf(args.directionLink, args.direction, args.busLineId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Links the binding to the fragment layout [fragment_bus_station_list.xml]
        binding = FragmentBusStationListBinding.inflate(layoutInflater)

        val busStationsAdapter = BusStationsAdapter()
        binding.busStationList.adapter = busStationsAdapter

        // Observes the busLines LiveData list from the viewmodel,
        // when changed it will update the recycleview adapter
        lifecycleScope.launch {
            busStationsViewModel
                .busStations
                .observe(viewLifecycleOwner, Observer { busStations ->
                    busStationsAdapter.submitList(busStations)
                })
        }

        // Sets a listener to receive callbacks whenever an item is clicked
        //busStationsViewModel.onBusStationClickListener = on

        return binding.root
    }
}
