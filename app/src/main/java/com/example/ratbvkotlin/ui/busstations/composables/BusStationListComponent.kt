package com.example.ratbvkotlin.ui.busstations.composables

import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.ratbvkotlin.ui.common.composables.ListItemDivider
import com.example.ratbvkotlin.viewmodels.BusStationsViewModel

@Composable
fun BusStationListComponent(
    busStations: List<BusStationsViewModel.BusStationItemViewModel>,
    navController: NavController) {

    LazyColumnForIndexed(
        items = busStations,
    ) { index, station ->

        BusStationItemComponent(
            station,
            navController
        )

        ListItemDivider(
            index,
            busStations.size
        )
    }
}