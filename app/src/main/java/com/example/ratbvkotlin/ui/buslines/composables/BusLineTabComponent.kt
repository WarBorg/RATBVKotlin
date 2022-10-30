package com.example.ratbvkotlin.ui.buslines.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ratbvkotlin.ui.common.composables.LastUpdateComposable
import com.example.ratbvkotlin.ui.common.composables.LoadingComponent
import com.example.ratbvkotlin.viewmodels.BusLinesViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun BusLinesTabComponent(
    busLinesFlow: SharedFlow<List<BusLinesViewModel.BusLineItemViewModel>>,
    lastUpdateDateFlow: StateFlow<String>,
    isRefreshingFlow: StateFlow<Boolean>,
    onPullToRefresh: () -> Unit,
    onBusLineClicked: (String, String, Long, String) -> Unit
) {

    val busLines by busLinesFlow.collectAsState(initial = emptyList())
    val lastUpdateDate by lastUpdateDateFlow.collectAsState(initial = "Never")
    val isRefreshing by isRefreshingFlow.collectAsState(initial = true)

    Column(
        modifier = Modifier.padding(
            start = 8.dp,
            top = 8.dp,
            end = 8.dp)
    ) {

        LastUpdateComposable(
            lastUpdateDate,
            modifier = Modifier
                .align(Alignment.End)
        )

        if (isRefreshing) {
            LoadingComponent()
        } 
        else if (busLines.isEmpty()) {
            Button(
                onClick = onPullToRefresh,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "REFRESH")
            }
        } else {
            BusLineListComponent(
                busLines,
                isRefreshingFlow,
                onPullToRefresh,
                onBusLineClicked
            )
        }
    }
}