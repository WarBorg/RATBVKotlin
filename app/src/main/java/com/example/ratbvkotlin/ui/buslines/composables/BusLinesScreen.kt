package com.example.ratbvkotlin.ui.buslines.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.common.composables.BusBottomNavigationComponent
import com.example.ratbvkotlin.ui.common.navigationGraphs.BusLinesBottomNavigationScreens
import com.example.ratbvkotlin.ui.common.navigationGraphs.BusLinesNavigationGraph
import com.example.ratbvkotlin.viewmodels.BusLinesViewModel
import com.example.ratbvkotlin.viewmodels.BusTransportSubtypes
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun BusLinesScreen(
    viewModel: BusLinesViewModel,
    onBusLineClicked: (String, String, Long, String) -> Unit
) {
    val internalNavHostController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    val bottomNavigationItems = listOf(
        BusLinesBottomNavigationScreens.Bus,
        BusLinesBottomNavigationScreens.Electricbus,
        BusLinesBottomNavigationScreens.Trolleybus
    )

    Scaffold(
        topBar = {
            BusLinesTopBarComponent(viewModel.busTransportSubtype)
        },
        content = { padding ->
            BusLinesNavigationGraph(
                internalNavHostController,
                viewModel,
                bottomNavigationItems,
                onLoadData = { transportSubtype ->
                    viewModel.updateViewedBusLineSubtype(transportSubtype)
                },
                onPullToRefresh = {
                    coroutineScope.launch {
                        viewModel.refreshBusLines(isForcedRefresh = true)
                    }
                },
                onBusLineClicked,
                modifier = Modifier
                    .padding(padding)
            )
        },
        bottomBar = {
            BusBottomNavigationComponent(
                internalNavHostController,
                bottomNavigationItems
            )
        }
    )
}

@Composable
fun BusLinesTopBarComponent(
    busTransportSubtypeFlow: StateFlow<BusTransportSubtypes>
) {
    val busTransportSubtype by busTransportSubtypeFlow.collectAsState(initial = BusTransportSubtypes.Bus)

    TopAppBar(
        title = {
            Text(text = when (busTransportSubtype) {
                BusTransportSubtypes.Bus -> stringResource(id = R.string.title_bus_lines)
                BusTransportSubtypes.Electricbus -> stringResource(id = R.string.title_electricbus_lines)
                BusTransportSubtypes.Trolleybus -> stringResource(id = R.string.title_trolleybus_lines)
            })
        }
    )
}