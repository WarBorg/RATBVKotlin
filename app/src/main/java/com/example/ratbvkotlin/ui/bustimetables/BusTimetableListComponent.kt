package com.example.ratbvkotlin.ui.bustimetables

import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BusTimetableListComponent(
    busTimetables: List<BusTimetablesViewModel.BusTimetableItemViewModel>
) {

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