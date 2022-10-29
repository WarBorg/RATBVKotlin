package com.example.ratbvkotlin.ui.buslines.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ratbvkotlin.ui.common.composables.ListItemDivider
import com.example.ratbvkotlin.viewmodels.BusLinesViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun BusLineListComponent(
    busLines: List<BusLinesViewModel.BusLineItemViewModel>,
    isRefreshingFlow: StateFlow<Boolean>,
    onPullToRefresh: () -> Unit,
    onBusLineClicked: (String, String, Int , String) -> Unit
) {
    val isRefreshing by isRefreshingFlow.collectAsState(initial = false)

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onPullToRefresh,
    ) {
        LazyColumn {
            itemsIndexed(busLines) { index, busLine ->

                BusLineItemComponent(
                    busLine.name,
                    busLine.route
                ) {
                    onBusLineClicked(
                        busLine.linkNormalWay,
                        busLine.linkReverseWay,
                        busLine.id,
                        busLine.name,
                    )
                }

                ListItemDivider(
                    index,
                    busLines.size
                )
            }
        }
    }
}