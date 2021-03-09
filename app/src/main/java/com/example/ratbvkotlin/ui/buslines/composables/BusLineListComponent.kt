package com.example.ratbvkotlin.ui.buslines.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.example.ratbvkotlin.ui.common.composables.ListItemDivider
import com.example.ratbvkotlin.viewmodels.BusLinesViewModel

@Composable
fun BusLineListComponent(
    busLines: List<BusLinesViewModel.BusLineItemViewModel>,
    onBusLineClicked: (String, String, Int , String) -> Unit
) {

    LazyColumn {
        itemsIndexed(busLines) { index, busLine ->

            BusLineItemComponent(
                busLine.name,
                busLine.route
            ) {
                onBusLineClicked(
                    busLine.linkNormalWay,
                    busLine.linkReverseWay,
                    busLine.id,
                    busLine.name,
                )
            }

            ListItemDivider(
                index,
                busLines.size
            )
        }
    }
}