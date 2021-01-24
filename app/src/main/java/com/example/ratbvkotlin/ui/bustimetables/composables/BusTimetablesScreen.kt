package com.example.ratbvkotlin.ui.bustimetables.composables

import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.common.BusBottomNavigationScreens
import com.example.ratbvkotlin.ui.common.composables.BusBottomNavigationComponent
import com.example.ratbvkotlin.viewmodels.BusTimetablesViewModel
import kotlinx.coroutines.launch

sealed class BusTimetablesBottomNavigationScreens(val timeOfWeek: String,
                                                  @StringRes val timeOfWeekResourceId: Int
) : BusBottomNavigationScreens(
    type = timeOfWeek,
    titleResourceId = timeOfWeekResourceId,
    iconResourceId = null
) {
    object WeekDays : BusTimetablesBottomNavigationScreens(
        timeOfWeek = "WeekDays",
        timeOfWeekResourceId = R.string.title_weekdays)
    object Saturday : BusTimetablesBottomNavigationScreens(
        timeOfWeek = "Saturday",
        timeOfWeekResourceId = R.string.title_saturday)
    object Sunday : BusTimetablesBottomNavigationScreens(
        timeOfWeek = "Sunday",
        timeOfWeekResourceId = R.string.title_sunday)
}

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
        bodyContent = {
            BusTimetablesNavHostComponent(
                navController,
                viewModel,
                bottomNavigationItems,
                onLoadData = {
                    coroutineScope.launch {
                        viewModel.getBusTimetables(it)
                    }
                }
            )
        },
        bottomBar = {
            BusBottomNavigationComponent(
                navController,
                bottomNavigationItems
            )
        }
    )
}

@Composable
fun BusTimetablesTopBarComponent(onBackNavigation: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.title_activity_bus_timetables))
        },
        navigationIcon = {
            IconButton(onClick = onBackNavigation) {
                Icon(androidx.compose.material.icons.Icons.Filled.ArrowBack)
            }
        }
    )
}

@Composable
fun BusTimetablesNavHostComponent(navController: NavHostController,
                                  viewModel: BusTimetablesViewModel,
                                  bottomNavigationTabs: List<BusBottomNavigationScreens>,
                                  onLoadData: (String) -> Unit
) {
    NavHost(
        navController,
        startDestination = BusTimetablesBottomNavigationScreens.WeekDays.timeOfWeek
    ) {
        bottomNavigationTabs.forEach { screen ->
            composable(screen.type) {

                onLoadData(screen.type)

                BusTimetableTabComponent(
                    viewModel.busTimetables,
                    viewModel.lastUpdated,
                    viewModel.isRefreshing
                )
            }
        }
    }
}