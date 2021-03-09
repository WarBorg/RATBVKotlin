package com.example.ratbvkotlin.ui.buslines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ratbvkotlin.ui.buslines.composables.BusLinesScreen
import com.example.ratbvkotlin.viewmodels.BusLinesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Shows the bus lines
 */
class BusLinesFragment : Fragment() {

    // Sets the viewmodel parameters with the necessary arguments
    private val busLinesViewModel: BusLinesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Navigation components
        val navController = findNavController()

        val onBusStationClicked: (String, String, Int , String) -> Unit = {
                linkNormalWay, linkReverseWay, busLineId, busLineName ->
            navController.navigate(
                BusLinesFragmentDirections
                    .navigateToBusStationsFragmentDest(
                        linkNormalWay,
                        linkReverseWay,
                        busLineId,
                        busLineName
                    )
            )
        }

        return ComposeView(requireContext()).apply {
            setContent {
                BusLinesScreen(
                    busLinesViewModel,
                    onBusStationClicked
                )
            }
        }
    }
}
