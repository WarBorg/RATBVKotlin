package com.example.ratbvkotlin.ui.busstations

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.databinding.FragmentBusStationListBinding
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Shows the bus stations for a specific bus line
 * List layout [fragment_bus_station_list.xml]
 * List item layout [fragment_bus_station_list_item.xml]
 */
class BusStationsFragment : Fragment() {

    private lateinit var binding: FragmentBusStationListBinding
    private lateinit var busStationsAdapter: BusStationsAdapter

    // Gets the arguments passed to the fragment
    private val args: BusStationsFragmentArgs by navArgs()
    // Sets the viewmodel parameters with the necessary arguments
    private val busStationsViewModel: BusStationsViewModel by viewModel {
        parametersOf(args.directionLinkNormal, args.directionLinkReverse, args.busLineId, args.busLineName)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Links the binding to the fragment layout [fragment_bus_station_list.xml]
        binding = FragmentBusStationListBinding.inflate(layoutInflater)

        // Uses Fragment.viewLifecycleOwner for LiveData Binding
        binding.lifecycleOwner = this
        // Sets the viewmodel for this page
        binding.busStationsViewModel = busStationsViewModel

        setHasOptionsMenu(true)

        setupRecyclerView()
        setupSupportActionBar()
        setupSwipeRefreshLayout()
        setupLiveDataObservers()

        // Sets a listener to receive callbacks whenever an item is clicked
        busStationsViewModel.onBusStationClickListener = onBusTimetableClickListener

        return binding.root
    }

    /**
     * Creates the adapter for the Recyclerview
     */
    private fun setupRecyclerView() {

        busStationsAdapter = BusStationsAdapter()
        binding.busStationListRecyclerview.adapter = busStationsAdapter
        binding.busStationListRecyclerview.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        )
    }

    /**
     * Sets the action bar title based on what bus line was chosen
     */
    private fun setupSupportActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title =
            "Bus stations for ${busStationsViewModel.busLineName}"
    }

    /**
     * Sets the behaviour when swiping to refresh
     */
    private fun setupSwipeRefreshLayout() {
        binding.busStationListSwiperefreshlayout.setOnRefreshListener {
            lifecycleScope.launch {
                busStationsViewModel.getBusStations(true)
            }
        }
    }

    /**
     * Sets observers for LiveData coming from the [BusStationsViewModel]
     */
    private fun setupLiveDataObservers() {
        lifecycleScope.launch {

            // Observes the busLines LiveData list from the viewmodel, when changed it will update the recyclerview adapter
            busStationsViewModel
                .busStations
                .observe(viewLifecycleOwner, Observer { busStations ->
                    busStationsAdapter.submitList(busStations)
                })

            // Observes the isRefreshing variable to show or hide the Swiperefreshlayout busy icon
            busStationsViewModel
                .isRefreshing
                .observe(viewLifecycleOwner, Observer { isRefreshing ->
                    binding.busStationListSwiperefreshlayout.isRefreshing = isRefreshing
                })

            // Gets the data when the fragment first loads
            busStationsViewModel.getBusStations()
        }
    }

    /**
     * Creates the menu options on the right of the toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.bus_stations_options_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Creates actions for different selected menu option
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.station_list_action_reverse -> {
                lifecycleScope.launch {
                    busStationsViewModel.reverseStations()
                }

                return true
            }
            R.id.station_list_action_download -> {
                lifecycleScope.launch {
                    busStationsViewModel.downloadStationsTimetables()

                    Toast
                        .makeText(
                            context,
                            "Download of timetables for all stations is complete",
                            Toast.LENGTH_LONG
                        )
                        .show()
                }

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Called when the view is destroyed
     */
    override fun onDestroyView() {
        // We clear the onItemClickListener in order to avoid any leaks
        busStationsViewModel.onBusStationClickListener = null
        super.onDestroyView()
    }

    /**
     * Called when an item is clicked in [BusStationsViewModel].
     */
    private val onBusTimetableClickListener: OnBusStationClickListener = { scheduleLink,
                                                                           busStationId,
                                                                           busStationName ->
        // Navigate to the bus timetables page
        findNavController()
            .navigate(BusStationsFragmentDirections
                .navigateToBusTimetablesActivityDest(scheduleLink, busStationId, busStationName))
    }
}
