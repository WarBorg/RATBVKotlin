package com.example.ratbvkotlin.ui.buslines.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.common.BusBottomNavigationScreens
import com.example.ratbvkotlin.ui.common.composables.BusBottomNavigationComponent
import com.example.ratbvkotlin.viewmodels.BusLinesViewModel
import kotlinx.coroutines.launch

sealed class BusLinesBottomNavigationScreens(val busType: String,
                                             @StringRes val transportSubtypeResource1Id: Int,
                                             @DrawableRes val transportSubtypeIconResourceId: Int
) : BusBottomNavigationScreens(
    type = busType,
    titleResourceId = transportSubtypeResource1Id,
    iconResourceId = transportSubtypeIconResourceId
) {
    object Bus : BusLinesBottomNavigationScreens(
        busType = "Bus",
        transportSubtypeResource1Id = R.string.title_bus,
        transportSubtypeIconResourceId = R.drawable.ic_tab_bus
    )
    object Trolleybus : BusLinesBottomNavigationScreens(
        busType = "Trolleybus",
        transportSubtypeResource1Id = R.string.title_trolleybus,
        transportSubtypeIconResourceId = R.drawable.ic_tab_trolleybus
    )
    object Midibus : BusLinesBottomNavigationScreens(
        busType = "Midibus",
        transportSubtypeResource1Id = R.string.title_midibus,
        transportSubtypeIconResourceId = R.drawable.ic_tab_midibus
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
        BusLinesBottomNavigationScreens.Trolleybus,
        BusLinesBottomNavigationScreens.Midibus,
    )

    Scaffold(
        topBar = {
            BusLinesTopBarComponent()
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
fun BusLinesTopBarComponent() {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.title_bus_lines))
        }
    )
}

@Composable
fun BusLinesNavHostComponent(navHostController: NavHostController,
                             viewModel: BusLinesViewModel,
                             bottomNavigationTabs: List<BusBottomNavigationScreens>,
                             onLoadData: (String) -> Unit,
                             onBusLineClicked: (String, String, Int , String) -> Unit
) {
    NavHost(
        navHostController,
        startDestination = BusLinesBottomNavigationScreens.Bus.busType
    ) {
        bottomNavigationTabs.forEach { screen ->
            composable(screen.type) {

                onLoadData(screen.type)

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