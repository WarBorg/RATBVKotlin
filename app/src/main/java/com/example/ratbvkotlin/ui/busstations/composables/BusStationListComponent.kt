package com.example.ratbvkotlin.ui.busstations.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ratbvkotlin.ui.common.composables.ListItemDivider
import com.example.ratbvkotlin.viewmodels.BusStationsViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun BusStationListComponent(
    busStations: List<BusStationsViewModel.BusStationItemViewModel>,
    isRefreshingFlow: StateFlow<Boolean>,
    onPullToRefresh: () -> Unit,
    onBusStationClicked: (String, Int , String) -> Unit
) {
    val isRefreshing by isRefreshingFlow.collectAsState(initial = false)

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onPullToRefresh,
    ) {
        LazyColumn {
            itemsIndexed(busStations) { index, station ->

                BusStationItemComponent(
                    station,
                    onBusStationClicked
                )

                ListItemDivider(
                    index,
                    busStations.size
                )
            }
        }
    }
}