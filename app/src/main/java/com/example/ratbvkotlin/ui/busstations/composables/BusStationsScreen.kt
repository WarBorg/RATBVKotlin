package com.example.ratbvkotlin.ui.busstations.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.common.composables.LastUpdateComposable
import com.example.ratbvkotlin.ui.common.composables.LoadingComponent
import com.example.ratbvkotlin.ui.resources.typography
import com.example.ratbvkotlin.viewmodels.BusStationsViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun BusStationsScreen(
    viewModel: BusStationsViewModel,
    onBackNavigation: () -> Unit,
    onBusStationClicked: (String, Int, String) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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
        content = { padding ->
            BusStationBodyComponent(
                viewModel.busStations,
                viewModel.lastUpdated,
                viewModel.isRefreshing,
                viewModel.isNormalDirection,
                viewModel.busLineName,
                onPullToRefresh = {
                    coroutineScope.launch {
                        viewModel.getBusStations(isForcedRefresh = true)
                    }
                },
                onBusStationClicked,
                modifier = Modifier
                    .padding(padding)
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
                text = stringResource(id = R.string.title_screen_bus_stations)
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackNavigation) {
                Icon(
                    imageVector =  Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onReverseStations) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_option_reverse
                    ),
                    contentDescription = "Reverse",
                    tint = Color.White
                )
            }
            IconButton(onClick = onDownloadAllTimetableData) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_option_download
                    ),
                    contentDescription = "Download",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun BusStationBodyComponent(busStationsFlow: StateFlow<List<BusStationsViewModel.BusStationItemViewModel>>,
                            lastUpdateDateFlow: StateFlow<String>,
                            isRefreshingFlow: StateFlow<Boolean>,
                            isNormalDirectionFlow: StateFlow<Boolean>,
                            busLineName: String,
                            onPullToRefresh: () -> Unit,
                            onBusStationClicked: (String, Int , String) -> Unit,
                            modifier: Modifier = Modifier) {

    val busStations by busStationsFlow.collectAsState(initial = emptyList())
    val lastUpdateDate by lastUpdateDateFlow.collectAsState(initial = "Never")
    val isRefreshing by isRefreshingFlow.collectAsState(initial = true)
    val isNormalDirection by isNormalDirectionFlow.collectAsState(initial = true)

    Column(
        modifier = modifier.padding(
            start = 8.dp,
            top = 8.dp,
            end = 8.dp,
            bottom = 8.dp
        )
    ) {

        LastUpdateComposable(
            lastUpdateDate,
            modifier = Modifier
                .align(Alignment.End)
        )

        Text(
            text = when (isNormalDirection) {
                true -> "$busLineName - normal"
                false -> "$busLineName - reverse"
            },
            style = typography.h5,
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(
                    top = 4.dp,
                    bottom = 4.dp
                )
                .fillMaxWidth()
        )

        if (isRefreshing) {
            LoadingComponent()
        } else {
            BusStationListComponent(
                busStations,
                isRefreshingFlow,
                onPullToRefresh,
                onBusStationClicked
            )
        }
    }
}