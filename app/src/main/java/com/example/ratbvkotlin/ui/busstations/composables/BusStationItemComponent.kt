package com.example.ratbvkotlin.ui.busstations.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ratbvkotlin.ui.resources.typography
import com.example.ratbvkotlin.viewmodels.BusStationsViewModel

@Composable
fun BusStationItemComponent(
    station: BusStationsViewModel.BusStationItemViewModel,
    onBusStationClicked: (String, Long, String) -> Unit
) {
    Text(
        text = station.name,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Normal,
        style = typography.h6,
        modifier = Modifier
            .clickable(onClick = {
                onBusStationClicked(
                    station.scheduleLink,
                    station.id,
                    station.name
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
}