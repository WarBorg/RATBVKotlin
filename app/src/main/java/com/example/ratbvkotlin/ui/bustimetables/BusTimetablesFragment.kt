package com.example.ratbvkotlin.ui.bustimetables

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.savedstate.SavedStateRegistry
import com.example.ratbvkotlin.data.models.BusTimetableModel
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

    private lateinit var scheduleLink: String
    private var busStationId: Int = 0
    private lateinit var timetableType: String

    // Sets the viewmodel parameters with the necessary arguments
    private val busTimetablesViewModel: BusTimetablesViewModel by viewModel {
        parametersOf(scheduleLink, busStationId, timetableType)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Links the binding to the fragment layout [fragment_bus_station_list.xml]
        binding = FragmentBusTimetableListBinding.inflate(layoutInflater)

        // Gets the passed data when navigating from the Activity
        scheduleLink = arguments?.getString(SCHEDULE_LINK_STRING) ?: ""
        busStationId = arguments?.getInt(BUS_STATION_ID_INT) ?: 0
        timetableType = arguments?.getString(TIMETABLE_TYPE_STRING) ?: ""

        // Uses Fragment.viewLifecycleOwner for LiveData Binding
        binding.lifecycleOwner = this
        // Sets the viewmodel for this page
        binding.busTimetablesViewModel = busTimetablesViewModel

        val busTimetablesAdapter = BusTimetablesAdapter()
        binding.busTimetableListRecyclerview.adapter = busTimetablesAdapter
        binding.busTimetableListRecyclerview.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        // Observes the busLines LiveData list from the viewmodel,
        // when changed it will update the recyclerview adapter
        lifecycleScope.launch {
            busTimetablesViewModel
                .busTimetables
                .observe(viewLifecycleOwner, Observer { busTimetables ->
                    busTimetablesAdapter.submitList(busTimetables)
                })
        }

        return binding.root
    }

    /**
     * Static constructor for the fragment with passed in values
     */
    companion object {
        private const val SCHEDULE_LINK_STRING = "scheduleLink"
        private const val BUS_STATION_ID_INT = "busStationId"
        private const val TIMETABLE_TYPE_STRING = "timetableType"

        fun newInstance(scheduleLink: String,
                        busStationId: Int,
                        timetableType: String) = BusTimetablesFragment().apply {
            arguments = bundleOf(
                SCHEDULE_LINK_STRING to scheduleLink,
                BUS_STATION_ID_INT to busStationId,
                TIMETABLE_TYPE_STRING to timetableType)
        }
    }
}