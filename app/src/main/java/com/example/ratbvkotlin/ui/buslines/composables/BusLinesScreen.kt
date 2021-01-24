package com.example.ratbvkotlin.ui.buslines.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.buslines.BusLinesFragmentDirections
import com.example.ratbvkotlin.ui.resources.typography
import com.example.ratbvkotlin.viewmodels.BusLinesViewModel
import kotlinx.coroutines.launch

sealed class BusLinesBottomNavigationScreens(val busType: String,
                                             @StringRes val titleResourceId: Int,
                                             @DrawableRes val iconResourceId: Int
) {
    object Bus : BusLinesBottomNavigationScreens(
        busType = "Bus",
        titleResourceId = R.string.title_bus,
        iconResourceId = R.drawable.ic_tab_bus
    )
    object Trolleybus : BusLinesBottomNavigationScreens(
        busType = "Trolleybus",
        titleResourceId = R.string.title_trolleybus,
        iconResourceId = R.drawable.ic_tab_trolleybus
    )
    object Midibus : BusLinesBottomNavigationScreens(
        busType = "Midibus",
        titleResourceId = R.string.title_midibus,
        iconResourceId = R.drawable.ic_tab_midibus
    )
}

@Composable
fun BusLinesScreen(
    viewModel: BusLinesViewModel,
    navController: NavController,
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
                navController,
                viewModel,
                onLoadData = {
                    coroutineScope.launch {
                        viewModel.getBusLines(it) }
                }
            )
        },
        bottomBar = {
            BusLinesBottomNavigationComponent(
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
                             navController: NavController,
                             viewModel: BusLinesViewModel,
                             onLoadData: (String) -> Unit
) {

    NavHost(
        navHostController,
        startDestination = BusLinesBottomNavigationScreens.Bus.busType
    ) {
        composable(BusLinesBottomNavigationScreens.Bus.busType) {

            onLoadData(BusLinesBottomNavigationScreens.Bus.busType)

            BusLinesTabComponent(
                viewModel.busLines,
                viewModel.lastUpdated,
                viewModel.isRefreshing,
                navController
            )
        }
        composable(BusLinesBottomNavigationScreens.Trolleybus.busType) {

            onLoadData(BusLinesBottomNavigationScreens.Trolleybus.busType)

            BusLinesTabComponent(
                viewModel.busLines,
                viewModel.lastUpdated,
                viewModel.isRefreshing,
                navController
            )
        }
        composable(BusLinesBottomNavigationScreens.Midibus.busType) {

            onLoadData(BusLinesBottomNavigationScreens.Midibus.busType)

            BusLinesTabComponent(
                viewModel.busLines,
                viewModel.lastUpdated,
                viewModel.isRefreshing,
                navController
            )
        }
    }
}

@Composable
fun BusLinesBottomNavigationComponent(
    navHostController: NavHostController,
    items: List<BusLinesBottomNavigationScreens>
) {
    BottomNavigation {
        val currentRoute = currentRoute(navHostController)

        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = vectorResource(
                            id = screen.iconResourceId
                        ),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = stringResource(
                            id = screen.titleResourceId
                        )
                    )
                },
                selected = currentRoute == screen.busType,
                onClick = {
                    // This is the equivalent to popUpTo the start destination
                    //navController.popBackStack(navController.graph.startDestination, false)
                    navHostController.popBackStack()

                    // This if check gives us a "singleTop" behavior where we do not create a
                    // second instance of the composable if we are already on that destination
                    if (currentRoute != screen.busType) {
                        navHostController.navigate(screen.busType)
                    }
                }
            )
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.arguments?.getString(KEY_ROUTE)
}

// ################################################
// ################################################
// ################################################

@Composable
fun BusLinesTabComponent(
    busLinesLiveData: LiveData<List<BusLinesViewModel.BusLineItemViewModel>>,
    lastUpdateDateLiveData: LiveData<String>,
    isRefreshingLiveData: LiveData<Boolean>,
    navController: NavController
) {

    val busLines by busLinesLiveData.observeAsState(initial = emptyList())
    val lastUpdateDate by lastUpdateDateLiveData.observeAsState(initial = "Never")
    val isRefreshing by isRefreshingLiveData.observeAsState(initial = true)

    Column(
        modifier = Modifier.padding(
            start = 8.dp,
            top = 8.dp,
            end = 8.dp,
            bottom = 58.dp)
    ) {

        Text(
            text = "${stringResource(id = R.string.last_update_simple)} $lastUpdateDate",
            fontWeight = FontWeight.Normal,
            style = typography.h6,
            modifier = Modifier.align(Alignment.End)
        )

        if (isRefreshing) {
            LoadingComponent()
        } else {
            BusLineListComponent(
                busLines,
                navController
            )
        }
    }
}

@Composable
fun BusLineListComponent(
    busLines: List<BusLinesViewModel.BusLineItemViewModel>,
    navController: NavController
) {

    LazyColumnForIndexed(
        items = busLines
    ) { index, busLine ->
        BusLineItemComponent(
            busLine.name,
            busLine.route
        ) {
            navController.navigate(
                BusLinesFragmentDirections
                    .navigateToBusStationsFragmentDest(
                        busLine.linkNormalWay,
                        busLine.linkReverseWay,
                        busLine.id,
                        busLine.name,
                    )
            )
        }

        if (index < busLines.size - 1) {
            Divider(
                color = Color.LightGray,
                thickness = 1.dp
            )
        }
    }
}

@Composable
fun LoadingComponent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

// ################################################
// ################################################
// ################################################

@Composable
fun BusLineItemComponent(
    name: String,
    route: String,
    onItemClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .clickable(onClick = onItemClick)
            .padding(
                top = 4.dp,
                bottom = 4.dp
            )
            .fillMaxWidth()
    ) {
        Text(
            text = name,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            style = typography.h5,
            modifier = Modifier
                .padding(4.dp)
        )
        Text(
            text = route,
            textAlign = TextAlign.Start,
            style = typography.subtitle1,
            modifier = Modifier
                .padding(
                    top = 4.dp,
                    bottom = 0.dp,
                    start = 4.dp,
                    end = 4.dp
                )
        )
    }
}