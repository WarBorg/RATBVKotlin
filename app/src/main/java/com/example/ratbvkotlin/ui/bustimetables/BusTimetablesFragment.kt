package com.example.ratbvkotlin.ui.bustimetables

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var busTimetablesAdapter: BusTimetablesAdapter

    private lateinit var scheduleLink: String
    private var busStationId: Int = 0
    private lateinit var busStationName: String
    private lateinit var timetableType: String

    // Sets the viewmodel parameters with the necessary arguments
    private val busTimetablesViewModel: BusTimetablesViewModel by viewModel {
        parametersOf(scheduleLink, busStationId, busStationName, timetableType)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Links the binding to the fragment layout [fragment_bus_station_list.xml]
        binding = FragmentBusTimetableListBinding.inflate(layoutInflater)

        setupBundleArguments()

        // Uses Fragment.viewLifecycleOwner for LiveData Binding
        binding.lifecycleOwner = this
        // Sets the viewmodel for this page
        binding.busTimetablesViewModel = busTimetablesViewModel

        setupRecyclerView()
        setupSupportActionBar()
        setupSwipeRefreshLayout()
        setupLiveDataObservers()

        return binding.root
    }

    /**
     * // Gets the passed data when navigating from the Activity
     */
    private fun setupBundleArguments() {

        scheduleLink = arguments?.getString(SCHEDULE_LINK_STRING) ?: ""
        busStationId = arguments?.getInt(BUS_STATION_ID_INT) ?: 0
        busStationName = arguments?.getString(BUS_STATION_NAME_STRING) ?: ""
        timetableType = arguments?.getString(TIMETABLE_TYPE_STRING) ?: ""
    }

    /**
     * Creates the adapter for the Recyclerview
     */
    private fun setupRecyclerView(): BusTimetablesAdapter {

        busTimetablesAdapter = BusTimetablesAdapter()
        binding.busTimetableListRecyclerview.adapter = busTimetablesAdapter
        binding.busTimetableListRecyclerview.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        )
        return busTimetablesAdapter
    }

    /**
     * Sets the action bar title based on what bus station was chosen
     */
    private fun setupSupportActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title =
            "Schedule for ${busTimetablesViewModel.busStationName}"
    }

    /**
     * Sets the behaviour when swiping to refresh
     */
    private fun setupSwipeRefreshLayout() {
        binding.busTimetableListSwiperefreshlayout.setOnRefreshListener {
            lifecycleScope.launch {
                busTimetablesViewModel.getBusTimetables("",true)
            }
        }
    }

    /**
     * Sets observers for LiveData coming from the [BusTimetablesViewModel]
     */
    private fun setupLiveDataObservers() {
        lifecycleScope.launch {

            // Observes the busLines LiveData list from the viewmodel, when changed it will update the recyclerview adapter
            busTimetablesViewModel
                .busTimetables
                .observe(viewLifecycleOwner, Observer { busTimetables ->
                    busTimetablesAdapter.submitList(busTimetables)
                })

            // Observes the isRefreshing variable to show or hide the Swiperefreshlayout busy icon
            busTimetablesViewModel
                .isRefreshing
                .observe(viewLifecycleOwner, Observer { isRefreshing ->
                    binding.busTimetableListSwiperefreshlayout.isRefreshing = isRefreshing
                })

            // Gets the data when the fragment first loads
            busTimetablesViewModel.getBusTimetables("")
        }
    }

    /**
     * Static constructor for the fragment with passed in values and constants
     */
    companion object {
        private const val SCHEDULE_LINK_STRING = "scheduleLink"
        private const val BUS_STATION_ID_INT = "busStationId"
        private const val BUS_STATION_NAME_STRING = "busStationName"
        private const val TIMETABLE_TYPE_STRING = "timetableType"

        fun newInstance(scheduleLink: String,
                        busStationId: Int,
                        busStationName: String,
                        timetableType: String) = BusTimetablesFragment().apply {
            arguments = bundleOf(
                SCHEDULE_LINK_STRING to scheduleLink,
                BUS_STATION_ID_INT to busStationId,
                BUS_STATION_NAME_STRING to busStationName,
                TIMETABLE_TYPE_STRING to timetableType)
        }
    }
}