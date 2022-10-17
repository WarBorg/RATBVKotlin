package com.example.ratbvkotlin.ui.common.navigationGraphs

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.bustimetables.composables.BusTimetableTabComponent
import com.example.ratbvkotlin.ui.common.BusBottomNavigationScreens
import com.example.ratbvkotlin.viewmodels.BusTimetablesViewModel
import com.example.ratbvkotlin.viewmodels.TimetableTypes

@Composable
fun BusTimetablesNavigationGraph(navController: NavHostController,
                                 viewModel: BusTimetablesViewModel,
                                 bottomNavigationTabs: List<BusBottomNavigationScreens>,
                                 onLoadData: (TimetableTypes) -> Unit,
                                 onPullToRefresh: (TimetableTypes) -> Unit,
                                 modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = BusTimetablesBottomNavigationScreens.WeekDays.timeOfWeek,
        modifier = modifier
    ) {
        bottomNavigationTabs.forEach { screen ->
            composable(screen.route) {

                val timetableType = TimetableTypes.valueOf(screen.route)

                onLoadData(timetableType)

                BusTimetableTabComponent(
                    viewModel.busTimetables,
                    viewModel.lastUpdated,
                    viewModel.isRefreshing,
                    viewModel.timeOfWeek,
                    viewModel.busStationName,
                    onPullToRefresh = { onPullToRefresh(timetableType) },
                )
            }
        }
    }
}

sealed class BusTimetablesBottomNavigationScreens(val timeOfWeek: String,
                                                  @StringRes val timeOfWeekResourceId: Int
) : BusBottomNavigationScreens(
    route = timeOfWeek,
    titleResourceId = timeOfWeekResourceId,
    iconResourceId = null
) {
    object WeekDays : BusTimetablesBottomNavigationScreens(
        timeOfWeek = TimetableTypes.WeekDays.name,
        timeOfWeekResourceId = R.string.title_weekdays)
    object Saturday : BusTimetablesBottomNavigationScreens(
        timeOfWeek = TimetableTypes.Saturday.name,
        timeOfWeekResourceId = R.string.title_saturday)
    object Sunday : BusTimetablesBottomNavigationScreens(
        timeOfWeek = TimetableTypes.Sunday.name,
        timeOfWeekResourceId = R.string.title_sunday)
}