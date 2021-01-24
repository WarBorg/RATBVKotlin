package com.example.ratbvkotlin.ui.busstations.composables

import android.widget.Toast
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.bustimetables.composables.LoadingComponent
import com.example.ratbvkotlin.ui.resources.typography
import com.example.ratbvkotlin.viewmodels.BusStationsViewModel
import kotlinx.coroutines.launch

@Composable
fun BusStationsScreen(viewModel: BusStationsViewModel,
                      navController: NavController,
                      onBackNavigation: () -> Unit) {

    val coroutineScope = rememberCoroutineScope()
    val context = AmbientContext.current

    coroutineScope.launch {
        viewModel.getBusStations()
    }

    Scaffold(
        topBar = {
            BusStationsTopBarComponent(
                onBackNavigation,
                onReverseStations = {
                    coroutineScope.launch {
                        viewModel.reverseStations()
                    }
                },
                onDownloadAllTimetableData = {
                    coroutineScope.launch {
                        viewModel.downloadStationsTimetables()

                        Toast
                            .makeText(
                                context,
                                R.string.bus_station_list_download_complete_message,
                                Toast.LENGTH_LONG
                            )
                            .show()
                    }
                }
            )
        },
        bodyContent = {
            BusStationBodyComponent(
                viewModel.busStations,
                viewModel.lastUpdated,
                viewModel.isRefreshing,
                navController
            )
        }
    )
}

@Composable
fun BusStationsTopBarComponent(
    onBackNavigation: () -> Unit,
    onReverseStations: () -> Unit,
    onDownloadAllTimetableData: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(
                    id = R.string.title_activity_bus_stations
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackNavigation) {
                Icon(Icons.Filled.ArrowBack)
            }
        },
        actions = {
            IconButton(onClick = onReverseStations) {
                Icon(
                    imageVector =  vectorResource(
                        id = R.drawable.ic_option_reverse
                    ),
                    tint = Color.White
                )
            }
            IconButton(onClick = onDownloadAllTimetableData) {
                Icon(
                    imageVector = vectorResource(
                        id = R.drawable.ic_option_download
                    ),
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun BusStationBodyComponent(busStationsLiveData: LiveData<List<BusStationsViewModel.BusStationItemViewModel>>,
                            lastUpdateDateLiveData: LiveData<String>,
                            isRefreshingLiveData: LiveData<Boolean>,
                            navController: NavController,
                            modifier: Modifier = Modifier) {

    val busStations by busStationsLiveData.observeAsState(initial = emptyList())
    val lastUpdateDate by lastUpdateDateLiveData.observeAsState(initial = "Never")
    val isRefreshing by isRefreshingLiveData.observeAsState(initial = true)

    Column(
        modifier = modifier.padding(
            start = 8.dp,
            top = 8.dp,
            end = 8.dp,
            bottom = 8.dp
        )
    ) {

        // TODO extract this as a separate component
        Text(
            text = "${stringResource(id = R.string.last_update_simple)} $lastUpdateDate",
            fontWeight = FontWeight.Normal,
            style = typography.h6,
            modifier = modifier
                .align(Alignment.End)
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