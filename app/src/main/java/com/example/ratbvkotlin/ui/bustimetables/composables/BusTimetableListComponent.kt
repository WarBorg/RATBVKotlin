package com.example.ratbvkotlin.ui.bustimetables.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ratbvkotlin.ui.common.composables.ListItemDivider
import com.example.ratbvkotlin.viewmodels.BusTimetablesViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun BusTimetableListComponent(
    busTimetables: List<BusTimetablesViewModel.BusTimetableItemViewModel>,
    isRefreshingFlow: StateFlow<Boolean>,
    onPullToRefresh: () -> Unit,
) {
    val isRefreshing by isRefreshingFlow.collectAsState(initial = false)

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onPullToRefresh,
    ) {
        LazyColumn {
            itemsIndexed(busTimetables) { index, timetable ->

                BusTimetableItemComponent(
                    timetable.hour,
                    timetable.minutes
                )

                ListItemDivider(
                    index,
                    busTimetables.size
                )
            }
        }
    }
}