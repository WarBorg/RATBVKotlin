package com.example.ratbvkotlin.ui.busstations.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.example.ratbvkotlin.ui.common.composables.ListItemDivider
import com.example.ratbvkotlin.viewmodels.BusStationsViewModel

@Composable
fun BusStationListComponent(
    busStations: List<BusStationsViewModel.BusStationItemViewModel>,
    onBusStationClicked: (String, Int , String) -> Unit
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