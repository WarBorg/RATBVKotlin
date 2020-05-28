package com.example.ratbvkotlin.ui.bustimetables

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.ratbvkotlin.databinding.FragmentBusTimetableListBinding
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Shows the bus stations for a specific bus line
 * List layout [fragment_bus_timetables_list.xml]
 * List item layout [fragment_bus_timetables_list_item.xml]
 */
class BusTimetablesFragment  : Fragment() {

    private lateinit var binding: FragmentBusTimetableListBinding

    // Gets the arguments passed to the fragment
    private val args: BusTimetablesFragmentArgs by navArgs()
    // Sets the viewmodel parameters with the necessary arguments
    private val busTimetablesViewModel: BusTimetablesViewModel by viewModel {
        parametersOf(args.scheduleLink, args.busStationId, args.timetableType)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Links the binding to the fragment layout [fragment_bus_station_list.xml]
        binding = FragmentBusTimetableListBinding.inflate(layoutInflater)

        val busTimetablesAdapter = BusTimetablesAdapter()
        //binding.busTimetablesList.adapter = busTimetablesAdapter

        // Observes the busLines LiveData list from the viewmodel,
        // when changed it will update the recycleview adapter
        lifecycleScope.launch {
            busTimetablesViewModel
                .busTimetables
                .observe(viewLifecycleOwner, Observer { busTimetables ->
                    busTimetablesAdapter.submitList(busTimetables)
                })
        }

        return binding.root
    }
}