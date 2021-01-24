package com.example.ratbvkotlin.ui.buslines.composables

import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.ratbvkotlin.ui.buslines.BusLinesFragmentDirections
import com.example.ratbvkotlin.ui.common.composables.ListItemDivider
import com.example.ratbvkotlin.viewmodels.BusLinesViewModel

@Composable
fun BusLineListComponent(
    busLines: List<BusLinesViewModel.BusLineItemViewModel>,
    navController: NavController
) {

    LazyColumnForIndexed(
        items = busLines
    ) { index, busLine ->
        BusLineItemComponent(
            busLine.name,
            busLine.route
        ) {
            navController.navigate(
                BusLinesFragmentDirections
                    .navigateToBusStationsFragmentDest(
                        busLine.linkNormalWay,
                        busLine.linkReverseWay,
                        busLine.id,
                        busLine.name,
                    )
            )
        }

        ListItemDivider(
            index,
            busLines.size
        )
    }
}