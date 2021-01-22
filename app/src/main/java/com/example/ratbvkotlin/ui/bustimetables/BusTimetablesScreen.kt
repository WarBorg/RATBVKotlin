package com.example.ratbvkotlin.ui.bustimetables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.resources.typography
import kotlinx.coroutines.launch

sealed class BusTimetablesBottomNavigationScreens(val timeOfWeek: String,
                                                  @StringRes val resourceId: Int) {
    object WeekDays : BusTimetablesBottomNavigationScreens(
        timeOfWeek = "WeekDays",
        resourceId = R.string.title_weekdays)
    object Saturday : BusTimetablesBottomNavigationScreens(
        timeOfWeek = "Saturday",
        resourceId = R.string.title_saturday)
    object Sunday : BusTimetablesBottomNavigationScreens(
        timeOfWeek = "Sunday",
        resourceId = R.string.title_sunday)
}

@Composable
fun BusTimetablesScreen(viewModel: BusTimetablesViewModel) {
    val navController = rememberNavController()

    val bottomNavigationItems = listOf(
        BusTimetablesBottomNavigationScreens.WeekDays,
        BusTimetablesBottomNavigationScreens.Saturday,
        BusTimetablesBottomNavigationScreens.Sunday,
    )

    Scaffold(
//        topBar = {
//            androidx.compose.material.TopAppBar(
//                title = {
//                    androidx.compose.material.Text(text = "Bus Time")
//                },
//                actions = {
//                    androidx.compose.material.IconButton(onClick = {}) {
//                        androidx.compose.material.Icon(androidx.compose.material.icons.Icons.Filled.Favorite)
//                    }
//                })
//        },
        bodyContent = {
            BusTimetablesNavigationConfigurations(navController, viewModel)
        },
        bottomBar = {
            BusTimetablesBottomNavigation(navController,
                                          bottomNavigationItems)
        }
    )
}

@Composable
fun BusTimetablesNavigationConfigurations(navController: NavHostController,
                                          viewModel: BusTimetablesViewModel) {
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController,
        startDestination = BusTimetablesBottomNavigationScreens.WeekDays.timeOfWeek
    ) {
        composable(BusTimetablesBottomNavigationScreens.WeekDays.timeOfWeek) {
            coroutineScope.launch {
                viewModel.getBusTimetables(BusTimetablesBottomNavigationScreens.WeekDays.timeOfWeek)
            }

            BusTimetablesListScreen(
                viewModel.busTimetables,
                viewModel.lastUpdated,
                viewModel.isRefreshing
            )
        }
        composable(BusTimetablesBottomNavigationScreens.Saturday.timeOfWeek) {
            coroutineScope.launch {
                viewModel.getBusTimetables(BusTimetablesBottomNavigationScreens.Saturday.timeOfWeek)
            }

            BusTimetablesListScreen(
                viewModel.busTimetables,
                viewModel.lastUpdated,
                viewModel.isRefreshing
            )
        }
        composable(BusTimetablesBottomNavigationScreens.Sunday.timeOfWeek) {
            coroutineScope.launch {
                viewModel.getBusTimetables(BusTimetablesBottomNavigationScreens.Sunday.timeOfWeek)
            }

            BusTimetablesListScreen(
                viewModel.busTimetables,
                viewModel.lastUpdated,
                viewModel.isRefreshing
            )
        }
    }
}

@Composable
fun BusTimetablesListScreen(hoursAndMinutesLiveData: LiveData<List<BusTimetablesViewModel.BusTimetableItemViewModel>>,
                            lastUpdateDateLiveData: LiveData<String>,
                            isRefreshingLiveData: LiveData<Boolean>,
                            modifier: Modifier = Modifier) {

    val hoursAndMinutesList by hoursAndMinutesLiveData.observeAsState(initial = emptyList())
    val lastUpdateDate by lastUpdateDateLiveData.observeAsState(initial = "Never")
    val isRefreshing by isRefreshingLiveData.observeAsState(initial = false)

    Column(
        modifier = Modifier.padding(8.dp)
    ) {

        Text(
            text = "Updated on $lastUpdateDate",
            style = typography.h6,
            modifier = Modifier.align(Alignment.End)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Hour",
                textAlign = TextAlign.Center,
                style = typography.h5,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.25f)
            )
            Text(
                text = "Minutes",
                textAlign = TextAlign.Center,
                style = typography.h5,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.75f)
            )
        }

        if (isRefreshing) {
            LiveDataLoadingComponent()
        } else {
            HoursAndMinutesScreen(hoursAndMinutesList)
        }
    }
}

@Composable
fun HoursAndMinutesScreen(
    hoursAndMinutesList: List<BusTimetablesViewModel.BusTimetableItemViewModel>) {

    LazyColumnFor(
        items = hoursAndMinutesList
    ) { item ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = item.hour,
                textAlign = TextAlign.Center,
                style = typography.h6,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.25f)
            )
            Text(
                text = item.minutes,
                textAlign = TextAlign.Start,
                style = typography.h6,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(0.75f)
            )
        }
    }
}

@Composable
fun BusTimetablesBottomNavigation(
    navController: NavHostController,
    items: List<BusTimetablesBottomNavigationScreens>
) {
    BottomNavigation {
        val currentRoute = currentRoute(navController)

        items.forEach { screen ->
            BottomNavigationItem(
                icon = { },
                //label = { Text(text = screen.resourceId) },
                label = { Text(text = stringResource(id = screen.resourceId)) },
                selected = currentRoute == screen.timeOfWeek,
                onClick = {
                    // This is the equivalent to popUpTo the start destination
                    //navController.popBackStack(navController.graph.startDestination, false)
                    navController.popBackStack()

                    // This if check gives us a "singleTop" behavior where we do not create a
                    // second instance of the composable if we are already on that destination
                    if (currentRoute != screen.timeOfWeek) {
                        navController.navigate(screen.timeOfWeek)
                    }
                }
            )
        }
    }
}

@Composable
fun LiveDataLoadingComponent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // A pre-defined composable that's capable of rendering a circular progress indicator. It
        // honors the Material Design specification.
        CircularProgressIndicator(modifier = Modifier.wrapContentWidth(CenterHorizontally))
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.arguments?.getString(KEY_ROUTE)
}

/*@Preview(
    showDecoration = true,
)
@Composable
fun timeTableScreen()
{
    BusTimetablesListScreen(listOf(
        Pair("07", "12, 14, 16, 20, 25, 30, 45"),
        Pair("08", "12, 14, 16, 20, 25, 30, 45"),
        Pair("09", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("10", "12, 14, 16, 20, 25, 30, 45"),
        Pair("20", "12, 14, 16, 20, 25, 30, 45"),
        Pair("20", "12, 14, 16, 20, 25, 30, 45"),
        Pair("20", "12, 14, 16, 20, 25, 30, 45"),
        Pair("20", "12, 14, 16, 20, 25, 30, 45"),
        Pair("20", "12, 14, 16, 20, 25, 30, 45"),
    ))*/
    //BusTimetablesScreen()
//}