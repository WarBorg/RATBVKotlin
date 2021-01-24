package com.example.ratbvkotlin.ui.bustimetables.composables

import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.runtime.Composable
import com.example.ratbvkotlin.ui.common.composables.ListItemDivider
import com.example.ratbvkotlin.viewmodels.BusTimetablesViewModel

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

        ListItemDivider(
            index,
            busTimetables.size
        )
    }
}