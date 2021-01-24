package com.example.ratbvkotlin.ui.bustimetables

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Shows the bus timetables for a specific bus station
 */
class BusTimetablesFragment : Fragment() {

    private val args: BusTimetablesFragmentArgs by navArgs()

    // Sets the viewmodel parameters with the necessary arguments
    private val busTimetablesViewModel: BusTimetablesViewModel by viewModel {
        parametersOf(
            args.scheduleLink,
            args.busStationId,
            args.busStationName,
            "WeekDays"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BusTimetablesScreen(
                    busTimetablesViewModel,
                    onBackNavigation = { })
            }
        }
    }
}