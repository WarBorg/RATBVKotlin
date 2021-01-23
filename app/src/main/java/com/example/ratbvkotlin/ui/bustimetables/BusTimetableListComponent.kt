package com.example.ratbvkotlin.ui.bustimetables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.resources.typography

@Composable
fun BusTimetablesListComponent(busTimetablesLiveData: LiveData<List<BusTimetablesViewModel.BusTimetableItemViewModel>>,
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

        Text(
            text = "${stringResource(id = R.string.last_update_simple)} $lastUpdateDate",
            fontWeight = FontWeight.Normal,
            style = typography.h6,
            modifier = Modifier.align(Alignment.End)
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

@Composable
fun BusTimetableListComponent(
    busTimetables: List<BusTimetablesViewModel.BusTimetableItemViewModel>) {

    LazyColumnForIndexed(
        items = busTimetables
    ) { index, timetable ->
        BusTimetableItemComponent(
            timetable.hour,
            timetable.minutes
        )

        if (index < busTimetables.size - 1) {
            Divider(
                color = Color.LightGray,
                thickness = 1.dp
            )
        }
    }
}

@Composable
fun LoadingComponent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentWidth(CenterHorizontally)
        )
    }
}