package com.example.ratbvkotlin.ui.buslines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ratbvkotlin.databinding.FragmentBusLineListBinding
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Shows the bus stations for a specific bus line
 * List layout [fragment_bus_line_list.xml]
 * List item layout [fragment_bus_line_list_item.xml]
 */
class BusLinesFragment : Fragment() {

    private lateinit var binding: FragmentBusLineListBinding

    // Gets the arguments passed to the fragment
    private val args: BusLinesFragmentArgs by navArgs()
    // Sets the viewmodel parameters with the necessary arguments
    private val busLinesViewModel: BusLinesViewModel by viewModel { parametersOf(args.busTransportSubtype) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Links the binding to the fragment layout [fragment_bus_line_list.xml]
        binding = FragmentBusLineListBinding.inflate(layoutInflater)

        // Uses Fragment.viewLifecycleOwner for LiveData Binding
        binding.lifecycleOwner = this
        // Sets the viewmodel for this page
        binding.busLinesViewModel = busLinesViewModel

        // Creates the adapter for the Recyclerview
        val busLinesAdapter = BusLinesAdapter()
        binding.busLineListRecyclerview.adapter = busLinesAdapter
        binding.busLineListRecyclerview.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        )

        // Sets the behaviour when swiping to refresh
        binding.busLineListSwiperefreshlayout.setOnRefreshListener {
            lifecycleScope.launch {
                busLinesViewModel.getBusLines()
            }
        }

        lifecycleScope.launch {

            // Observes the busLines LiveData list from the viewmodel, when changed it will update the recyclerview adapter
            busLinesViewModel
                .busLines
                .observe(viewLifecycleOwner, Observer { busLines ->
                    busLinesAdapter.submitList(busLines)
            })

            // Observes the isRefreshing variable to show or hide the Swiperefreshlayout busy icon
            busLinesViewModel
                .isRefreshing
                .observe(viewLifecycleOwner, Observer { isRefreshing ->
                    binding.busLineListSwiperefreshlayout.isRefreshing = isRefreshing
                })

            // Gets the data when the fragment first loads
            busLinesViewModel.getBusLines()
        }

        // Sets a listener to receive callbacks whenever an item is clicked
        busLinesViewModel.onBusLineClickListener = onBusLineClickListener

        return binding.root
    }

    /**
     * Called when the view is destroyed
     */
    override fun onDestroyView() {
        // We clear the onItemClickListener in order to avoid any leaks
        busLinesViewModel.onBusLineClickListener = null
        super.onDestroyView()
    }

    /**
     * Called when an item is clicked in [BusLinesViewModel].
     */
    private val onBusLineClickListener: OnBusLineClickListener = { directionLink, direction, busLineId ->
        // Navigate to the bus stations page
        findNavController()
            .navigate(BusLinesFragmentDirections
                .navigateToBusStationsActivityDest(directionLink, direction, busLineId))
    }
}
