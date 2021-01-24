package com.example.ratbvkotlin.ui.busstations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ratbvkotlin.ui.resources.typography

@Composable
fun BusStationListComponent(
    busStations: List<BusStationsViewModel.BusStationItemViewModel>,
    navController: NavController) {

    LazyColumnForIndexed(
        items = busStations,
    ) { index, station ->

        Text(
            text = station.name,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal,
            style = typography.h6,
            modifier = Modifier
                .clickable(onClick = {
                    navController.navigate(
                        BusStationsFragmentDirections
                            .navigateToBusTimetablesFragmentDest(
                                station.scheduleLink,
                                station.id,
                                station.name
                            )
                    )
                })
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 4.dp,
                    end = 4.dp
                )
                .fillMaxWidth()
        )

        if (index < busStations.size - 1) {
            Divider(
                color = Color.LightGray,
                thickness = 1.dp
            )
        }
    }
}