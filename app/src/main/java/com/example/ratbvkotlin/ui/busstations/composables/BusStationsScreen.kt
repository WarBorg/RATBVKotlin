package com.example.ratbvkotlin.ui.busstations.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.common.composables.LastUpdateComposable
import com.example.ratbvkotlin.ui.common.composables.LoadingComponent
import com.example.ratbvkotlin.viewmodels.BusStationsViewModel
import kotlinx.coroutines.launch

@Composable
fun BusStationsScreen(
    viewModel: BusStationsViewModel,
    onBackNavigation: () -> Unit,
    onBusStationClicked: (String, Int , String) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val context = AmbientContext.current

    Scaffold(
        topBar = {
            BusStationsTopBarComponent(
                viewModel.busLineName,
                viewModel.isNormalDirection,
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
                onBusStationClicked
            )
        }
    )
}

@Composable
fun BusStationsTopBarComponent(
    busLineName: String,
    isNormalDirectionLiveData: LiveData<Boolean>,
    onBackNavigation: () -> Unit,
    onReverseStations: () -> Unit,
    onDownloadAllTimetableData: () -> Unit
) {

    val isNormalDirection by isNormalDirectionLiveData.observeAsState(initial = true)

    TopAppBar(
        title = {
            Text(
                text = when (isNormalDirection) {
                    true -> "$busLineName - normal"
                    false -> "$busLineName - reverse"
                }
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
                            onBusStationClicked: (String, Int , String) -> Unit,
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

        LastUpdateComposable(
            lastUpdateDate,
            modifier = Modifier
                .align(Alignment.End)
        )

        if (isRefreshing) {
            LoadingComponent()
        } else {
            BusStationListComponent(
                busStations,
                onBusStationClicked
            )
        }
    }
}