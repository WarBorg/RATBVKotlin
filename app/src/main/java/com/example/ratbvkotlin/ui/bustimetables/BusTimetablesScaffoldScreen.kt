package com.example.ratbvkotlin.ui.bustimetables

import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.ratbvkotlin.R
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
fun BusTimetablesScaffoldScreen(viewModel: BusTimetablesViewModel,
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
                onLoadData = {
                    coroutineScope.launch {
                        viewModel.getBusTimetables(it) }
                }
            )
        },
        bottomBar = {
            BusTimetablesBottomNavigationComponent(navController,
                bottomNavigationItems)
        }
    )
}

@Composable
fun BusTimetablesTopBarComponent(onBackNavigation: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "Bus Timetables")
        },
        navigationIcon = {
            IconButton(onClick = onBackNavigation) {
                Icon(androidx.compose.material.icons.Icons.Filled.ArrowBack)
            }
        })
}

@Composable
fun BusTimetablesNavHostComponent(navController: NavHostController,
                                  viewModel: BusTimetablesViewModel,
                                  onLoadData: (String) -> Unit) {

    NavHost(
        navController,
        startDestination = BusTimetablesBottomNavigationScreens.WeekDays.timeOfWeek
    ) {
        composable(BusTimetablesBottomNavigationScreens.WeekDays.timeOfWeek) {

            onLoadData(BusTimetablesBottomNavigationScreens.WeekDays.timeOfWeek)

            BusTimetablesListScreen(
                viewModel.busTimetables,
                viewModel.lastUpdated,
                viewModel.isRefreshing
            )
        }
        composable(BusTimetablesBottomNavigationScreens.Saturday.timeOfWeek) {

            onLoadData(BusTimetablesBottomNavigationScreens.Saturday.timeOfWeek)

            BusTimetablesListScreen(
                viewModel.busTimetables,
                viewModel.lastUpdated,
                viewModel.isRefreshing
            )
        }
        composable(BusTimetablesBottomNavigationScreens.Sunday.timeOfWeek) {

            onLoadData(BusTimetablesBottomNavigationScreens.Sunday.timeOfWeek)

            BusTimetablesListScreen(
                viewModel.busTimetables,
                viewModel.lastUpdated,
                viewModel.isRefreshing
            )
        }
    }
}

@Composable
fun BusTimetablesBottomNavigationComponent(
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
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.arguments?.getString(KEY_ROUTE)
}