package com.example.ratbvkotlin.ui.bustimetables.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.common.composables.BusBottomNavigationComponent
import com.example.ratbvkotlin.ui.common.navigationGraphs.BusTimetablesBottomNavigationScreens
import com.example.ratbvkotlin.ui.common.navigationGraphs.BusTimetablesNavigationGraph
import com.example.ratbvkotlin.viewmodels.BusTimetablesViewModel
import kotlinx.coroutines.launch

@Composable
fun BusTimetablesScreen(viewModel: BusTimetablesViewModel,
                        onBackNavigation: () -> Unit) {

    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    val bottomNavigationItems = listOf(
        BusTimetablesBottomNavigationScreens.WeekDays,
        BusTimetablesBottomNavigationScreens.Saturday,
        BusTimetablesBottomNavigationScreens.Sunday,
    )

    Scaffold(
        topBar = {
            BusTimetablesTopBarComponent(
                onBackNavigation
            )
        },
        content = { padding ->
            BusTimetablesNavigationGraph(
                navController,
                viewModel,
                bottomNavigationItems,
                onLoadData = { timetableType ->
                    coroutineScope.launch {
                        viewModel.updateViewedTimeOfWeek(timetableType)
                    }
                },
                onPullToRefresh = {
                    coroutineScope.launch {
                        viewModel.refreshBusTimetables(
                            isForcedRefresh = true
                        )
                    }
                },
                modifier = Modifier
                    .padding(padding)
            )
        },
        bottomBar = {
            BusBottomNavigationComponent(
                navController,
                bottomNavigationItems
            )
        },
    )
}

@Composable
fun BusTimetablesTopBarComponent(
    onBackNavigation: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.title_screen_bus_timetables))
        },
        navigationIcon = {
            IconButton(onClick = onBackNavigation) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.ArrowBack,
                    contentDescription = null)
            }
        },
    )
}