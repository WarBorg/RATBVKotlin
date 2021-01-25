package com.example.ratbvkotlin.ui.buslines.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.common.BusBottomNavigationScreens
import com.example.ratbvkotlin.ui.common.composables.BusBottomNavigationComponent
import com.example.ratbvkotlin.viewmodels.BusLinesViewModel
import com.example.ratbvkotlin.viewmodels.BusTransportSubtypes
import kotlinx.coroutines.launch

sealed class BusLinesBottomNavigationScreens(val busTransportSubtype: String,
                                             @StringRes val transportSubtypeResource1Id: Int,
                                             @DrawableRes val transportSubtypeIconResourceId: Int
) : BusBottomNavigationScreens(
    type = busTransportSubtype,
    titleResourceId = transportSubtypeResource1Id,
    iconResourceId = transportSubtypeIconResourceId
) {
    object Bus : BusLinesBottomNavigationScreens(
        busTransportSubtype = BusTransportSubtypes.Bus.name,
        transportSubtypeResource1Id = R.string.title_bus,
        transportSubtypeIconResourceId = R.drawable.ic_tab_bus
    )
    object Electricbus : BusLinesBottomNavigationScreens(
        busTransportSubtype = BusTransportSubtypes.Electricbus.name,
        transportSubtypeResource1Id = R.string.title_electricbus,
        transportSubtypeIconResourceId = R.drawable.ic_tab_electricbus
    )
    object Trolleybus : BusLinesBottomNavigationScreens(
        busTransportSubtype = BusTransportSubtypes.Trolleybus.name,
        transportSubtypeResource1Id = R.string.title_trolleybus,
        transportSubtypeIconResourceId = R.drawable.ic_tab_trolleybus
    )
}

@Composable
fun BusLinesScreen(
    viewModel: BusLinesViewModel,
    onBusLineClicked: (String, String, Int , String) -> Unit
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
        bodyContent = {
            BusLinesNavHostComponent(
                internalNavHostController,
                viewModel,
                bottomNavigationItems,
                onLoadData = { transportSubtype ->
                    coroutineScope.launch {
                        viewModel.getBusLines(transportSubtype)
                    }
                },
                onBusLineClicked
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
    busTransportSubtypeLiveData: LiveData<BusTransportSubtypes>
) {
    val busTransportSubtype by busTransportSubtypeLiveData.observeAsState(initial = BusTransportSubtypes.Bus)

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

@Composable
fun BusLinesNavHostComponent(navHostController: NavHostController,
                             viewModel: BusLinesViewModel,
                             bottomNavigationTabs: List<BusBottomNavigationScreens>,
                             onLoadData: (BusTransportSubtypes) -> Unit,
                             onBusLineClicked: (String, String, Int , String) -> Unit
) {
    NavHost(
        navHostController,
        startDestination = BusLinesBottomNavigationScreens.Bus.busTransportSubtype
    ) {
        bottomNavigationTabs.forEach { screen ->
            composable(screen.type) {

                onLoadData(
                    BusTransportSubtypes.valueOf(screen.type)
                )

                BusLinesTabComponent(
                    viewModel.busLines,
                    viewModel.lastUpdated,
                    viewModel.isRefreshing,
                    onBusLineClicked
                )
            }
        }
    }
}