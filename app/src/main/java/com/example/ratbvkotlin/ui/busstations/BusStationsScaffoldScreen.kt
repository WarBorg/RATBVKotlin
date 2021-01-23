package com.example.ratbvkotlin.ui.busstations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.bustimetables.LoadingComponent
import com.example.ratbvkotlin.ui.resources.typography
import kotlinx.coroutines.launch

@Composable
fun BusStationsScaffoldScreen(viewModel: BusStationsViewModel,
                              navController: NavController,
                              onBackNavigation: () -> Unit) {

    val coroutineScope = rememberCoroutineScope()

    coroutineScope.launch {
        viewModel.getBusStations() }

    Scaffold(
        topBar = {
            BusStationsTopBarComponent(
                onBackNavigation
            )
        },
        bodyContent = {
            BusStationListScreen(
                viewModel.busStations,
                viewModel.lastUpdated,
                viewModel.isRefreshing,
                navController
            )
        }
    )
}

@Composable
fun BusStationsTopBarComponent(onBackNavigation: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.title_activity_bus_stations))
        },
        navigationIcon = {
            IconButton(onClick = onBackNavigation) {
                Icon(Icons.Filled.ArrowBack)
            }
        }
    )
}

@Composable
fun BusStationListScreen(busStationsLiveData: LiveData<List<BusStationsViewModel.BusStationItemViewModel>>,
                         lastUpdateDateLiveData: LiveData<String>,
                         isRefreshingLiveData: LiveData<Boolean>,
                         navController: NavController,
                         modifier: Modifier = Modifier) {

    val busStations by busStationsLiveData.observeAsState(initial = emptyList())
    val lastUpdateDate by lastUpdateDateLiveData.observeAsState(initial = "Never")
    val isRefreshing by isRefreshingLiveData.observeAsState(initial = true)

    Column(
        modifier = Modifier.padding(
            start = 8.dp,
            top = 8.dp,
            end = 8.dp,
            bottom = 58.dp
        )
    ) {

        // TODO extract this as a separate component
        Text(
            text = "${stringResource(id = R.string.last_update_simple)} $lastUpdateDate",
            fontWeight = FontWeight.Normal,
            style = typography.h6,
            modifier = Modifier.align(Alignment.End)
        )

        if (isRefreshing) {
            LoadingComponent()
        } else {
            BusStationListComponent(
                busStations,
                navController
            )
        }
    }
}

@Composable
fun BusStationListComponent(
    busStations: List<BusStationsViewModel.BusStationItemViewModel>,
    navController: NavController) {

    LazyColumnForIndexed(
        items = busStations,
    ) { index, station ->

        Text(
            text = station.busStationName,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            style = typography.h6,
            modifier = Modifier
                .clickable(onClick = {
                    navController.navigate(
                        BusStationsFragmentDirections
                            .navigateToBusTimetablesFragmentDest(
                                station.scheduleLink,
                                station.busStationId,
                                station.busStationName
                            )
                    )
                })
                .padding(4.dp)
        )

        if (index < busStations.size - 1) {
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