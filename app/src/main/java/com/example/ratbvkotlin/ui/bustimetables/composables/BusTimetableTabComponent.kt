package com.example.ratbvkotlin.ui.bustimetables.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ratbvkotlin.ui.common.composables.LastUpdateComposable
import com.example.ratbvkotlin.ui.common.composables.LoadingComponent
import com.example.ratbvkotlin.ui.resources.typography
import com.example.ratbvkotlin.viewmodels.BusTimetablesViewModel
import com.example.ratbvkotlin.viewmodels.TimetableTypes
import kotlinx.coroutines.flow.StateFlow

@Composable
fun BusTimetableTabComponent(
    busTimetablesFlow: StateFlow<List<BusTimetablesViewModel.BusTimetableItemViewModel>>,
    lastUpdateDateFlow: StateFlow<String>,
    isRefreshingFlow: StateFlow<Boolean>,
    timeOfWeekFlow: StateFlow<TimetableTypes>,
    busStationName: String,
    onPullToRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {

    val busTimetables by busTimetablesFlow.collectAsState(initial = emptyList())
    val lastUpdateDate by lastUpdateDateFlow.collectAsState(initial = "Never")
    val isRefreshing by isRefreshingFlow.collectAsState(initial = true)
    val timeOfWeek by timeOfWeekFlow.collectAsState(initial = TimetableTypes.WeekDays)

    Column(
            modifier = modifier.padding(
                start = 8.dp,
                top = 8.dp,
                end = 8.dp)
            ) {

        LastUpdateComposable(
            lastUpdateDate,
            modifier = Modifier
                .align(Alignment.End)
        )

        Text(
            text = "$busStationName - $timeOfWeek",
            style = typography.h5,
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(
                    top = 4.dp,
                    bottom = 4.dp
                )
                .fillMaxWidth()
        )

        BusTimetableListHeaderComponent()

        if (isRefreshing) {
            LoadingComponent()
        } else {
            BusTimetableListComponent(
                busTimetables,
                isRefreshingFlow,
                onPullToRefresh
            )
        }
    }
}

@Composable
fun BusTimetableListHeaderComponent() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Hour",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = typography.h6,
            modifier = Modifier
                .padding(4.dp)
                .weight(0.25f)
        )
        Text(
            text = "Minutes",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = typography.h6,
            modifier = Modifier
                .padding(4.dp)
                .weight(0.75f)
        )
    }
}