package com.example.ratbvkotlin.ui.buslines.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.ratbvkotlin.ui.common.composables.LastUpdateComposable
import com.example.ratbvkotlin.ui.common.composables.LoadingComponent
import com.example.ratbvkotlin.viewmodels.BusLinesViewModel

@Composable
fun BusLinesTabComponent(
    busLinesLiveData: LiveData<List<BusLinesViewModel.BusLineItemViewModel>>,
    lastUpdateDateLiveData: LiveData<String>,
    isRefreshingLiveData: LiveData<Boolean>,
    onBusLineClicked: (String, String, Int , String) -> Unit
) {

    val busLines by busLinesLiveData.observeAsState(initial = emptyList())
    val lastUpdateDate by lastUpdateDateLiveData.observeAsState(initial = "Never")
    val isRefreshing by isRefreshingLiveData.observeAsState(initial = true)

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
        } else {
            BusLineListComponent(
                busLines,
                onBusLineClicked
            )
        }
    }
}