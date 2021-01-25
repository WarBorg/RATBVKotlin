package com.example.ratbvkotlin.ui.bustimetables.composables

import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.common.BusBottomNavigationScreens
import com.example.ratbvkotlin.ui.common.composables.BusBottomNavigationComponent
import com.example.ratbvkotlin.viewmodels.BusTimetablesViewModel
import com.example.ratbvkotlin.viewmodels.TimetableTypes
import kotlinx.coroutines.launch

sealed class BusTimetablesBottomNavigationScreens(val timeOfWeek: String,
                                                  @StringRes val timeOfWeekResourceId: Int
) : BusBottomNavigationScreens(
    type = timeOfWeek,
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
                viewModel.busStationName,
                viewModel.timeOfWeek,
                onBackNavigation
            )
        },
        bodyContent = {
            BusTimetablesNavHostComponent(
                navController,
                viewModel,
                bottomNavigationItems,
                onLoadData = { timetableType ->
                    coroutineScope.launch {
                        viewModel.getBusTimetables(timetableType)
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
fun BusTimetablesTopBarComponent(
    busStationName: String,
    timeOfWeekLiveData: LiveData<TimetableTypes>,
    onBackNavigation: () -> Unit
) {

    val timeOfWeek by timeOfWeekLiveData.observeAsState(initial = TimetableTypes.WeekDays)

    TopAppBar(
        title = {
            Text(text = "$busStationName - $timeOfWeek")
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
                                  onLoadData: (TimetableTypes) -> Unit
) {
    NavHost(
        navController,
        startDestination = BusTimetablesBottomNavigationScreens.WeekDays.timeOfWeek
    ) {
        bottomNavigationTabs.forEach { screen ->
            composable(screen.type) {

                onLoadData(
                    TimetableTypes.valueOf(screen.type)
                )

                BusTimetableTabComponent(
                    viewModel.busTimetables,
                    viewModel.lastUpdated,
                    viewModel.isRefreshing
                )
            }
        }
    }
}