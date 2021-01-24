package com.example.ratbvkotlin.ui.bustimetables.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.ratbvkotlin.ui.common.composables.LastUpdateComposable
import com.example.ratbvkotlin.ui.common.composables.LoadingComponent
import com.example.ratbvkotlin.ui.resources.typography
import com.example.ratbvkotlin.viewmodels.BusTimetablesViewModel

@Composable
fun BusTimetableTabComponent(busTimetablesLiveData: LiveData<List<BusTimetablesViewModel.BusTimetableItemViewModel>>,
                             lastUpdateDateLiveData: LiveData<String>,
                             isRefreshingLiveData: LiveData<Boolean>,
                             modifier: Modifier = Modifier) {

    val busTimetables by busTimetablesLiveData.observeAsState(initial = emptyList())
    val lastUpdateDate by lastUpdateDateLiveData.observeAsState(initial = "Never")
    val isRefreshing by isRefreshingLiveData.observeAsState(initial = true)

    Column(
            modifier = Modifier.padding(
                start = 8.dp,
                top = 8.dp,
                end = 8.dp,
                bottom = 58.dp)
            ) {

        LastUpdateComposable(
            lastUpdateDate,
            modifier = Modifier
                .align(Alignment.End)
        )

        BusTimetableListHeaderComponent()

        if (isRefreshing) {
            LoadingComponent()
        } else {
            BusTimetableListComponent(busTimetables)
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