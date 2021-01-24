package com.example.ratbvkotlin.ui.busstations

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ratbvkotlin.ui.busstations.composables.BusStationsScreen
import com.example.ratbvkotlin.viewmodels.BusStationsViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Shows the bus stations for a specific bus line
 */
class BusStationsFragment : Fragment() {

    // Gets the arguments passed to the fragment
    private val args: BusStationsFragmentArgs by navArgs()

    // Sets the viewmodel parameters with the necessary arguments
    private val busStationsViewModel: BusStationsViewModel by viewModel {
        parametersOf(
            args.directionLinkNormal,
            args.directionLinkReverse,
            args.busLineId,
            args.busLineName)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Navigation components
        val navController = findNavController()

        val onBackNavigation: () -> Unit = {
            navController.popBackStack()
        }

        val onBusStationClicked: (String, Int , String) -> Unit = {
            scheduleLink, stationId, stationName ->
                navController.navigate(
                    BusStationsFragmentDirections
                        .navigateToBusTimetablesFragmentDest(
                            scheduleLink,
                            stationId,
                            stationName
                        )
                )
            }

        return ComposeView(requireContext()).apply {
            setContent {
                BusStationsScreen(
                    busStationsViewModel,
                    onBackNavigation,
                    onBusStationClicked
                )
            }
        }
    }

    /**
     * Sets the action bar title based on what bus line was chosen
     */
    /*private fun setupSupportActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title =
            "Bus stations for ${busStationsViewModel.busLineName}"
    }*/
}
