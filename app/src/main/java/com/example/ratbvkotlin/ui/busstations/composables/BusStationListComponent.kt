package com.example.ratbvkotlin.ui.busstations.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import com.example.ratbvkotlin.ui.common.composables.ListItemDivider
import com.example.ratbvkotlin.viewmodels.BusStationsViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun BusStationListComponent(
    busStations: List<BusStationsViewModel.BusStationItemViewModel>,
    isRefreshingLiveData: LiveData<Boolean>,
    onPullToRefresh: () -> Unit,
    onBusStationClicked: (String, Int , String) -> Unit
) {
    val isRefreshing by isRefreshingLiveData.observeAsState(initial = false)

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