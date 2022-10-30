package com.example.ratbvkotlin.ui.common.navigationGraphs

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.buslines.composables.BusLinesTabComponent
import com.example.ratbvkotlin.ui.common.BusBottomNavigationScreens
import com.example.ratbvkotlin.viewmodels.BusLinesViewModel
import com.example.ratbvkotlin.viewmodels.BusTransportSubtypes

@Composable
fun BusLinesNavigationGraph(navHostController: NavHostController,
                            viewModel: BusLinesViewModel,
                            bottomNavigationTabs: List<BusBottomNavigationScreens>,
                            onLoadData: (BusTransportSubtypes) -> Unit,
                            onPullToRefresh: () -> Unit,
                            onBusLineClicked: (String, String, Long, String) -> Unit,
                            modifier: Modifier = Modifier,
) {
    NavHost(
        navHostController,
        startDestination = BusLinesBottomNavigationScreens.Bus.route,
        modifier = modifier
    ) {
        bottomNavigationTabs.forEach { screen ->
            composable(screen.route) {

                val busTransportSubtype = BusTransportSubtypes.valueOf(screen.route)

                onLoadData(busTransportSubtype)

                BusLinesTabComponent(
                    viewModel.busLines,
                    viewModel.lastUpdated,
                    viewModel.isRefreshing,
                    onPullToRefresh = { onPullToRefresh() },
                    onBusLineClicked
                )
            }
        }
    }
}

sealed class BusLinesBottomNavigationScreens(val busTransportSubtype: String,
                                             @StringRes val transportSubtypeResourceId: Int,
                                             @DrawableRes val transportSubtypeIconResourceId: Int
) : BusBottomNavigationScreens(
    route = busTransportSubtype,
    titleResourceId = transportSubtypeResourceId,
    iconResourceId = transportSubtypeIconResourceId
) {
    object Bus : BusLinesBottomNavigationScreens(
        busTransportSubtype = BusTransportSubtypes.Bus.name,
        transportSubtypeResourceId = R.string.title_bus,
        transportSubtypeIconResourceId = R.drawable.ic_tab_bus
    )
    object Electricbus : BusLinesBottomNavigationScreens(
        busTransportSubtype = BusTransportSubtypes.Electricbus.name,
        transportSubtypeResourceId = R.string.title_electricbus,
        transportSubtypeIconResourceId = R.drawable.ic_tab_electricbus
    )
    object Trolleybus : BusLinesBottomNavigationScreens(
        busTransportSubtype = BusTransportSubtypes.Trolleybus.name,
        transportSubtypeResourceId = R.string.title_trolleybus,
        transportSubtypeIconResourceId = R.drawable.ic_tab_trolleybus
    )
}