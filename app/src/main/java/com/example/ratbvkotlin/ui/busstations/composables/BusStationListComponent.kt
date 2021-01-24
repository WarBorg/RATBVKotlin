package com.example.ratbvkotlin.ui.busstations.composables

import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.runtime.Composable
import com.example.ratbvkotlin.ui.common.composables.ListItemDivider
import com.example.ratbvkotlin.viewmodels.BusStationsViewModel

@Composable
fun BusStationListComponent(
    busStations: List<BusStationsViewModel.BusStationItemViewModel>,
    onBusStationClicked: (String, Int , String) -> Unit
) {

    LazyColumnForIndexed(
        items = busStations,
    ) { index, station ->

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