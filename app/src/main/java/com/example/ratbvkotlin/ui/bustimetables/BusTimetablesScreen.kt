package com.example.ratbvkotlin.ui.bustimetables

import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.DirectionsRailway
import androidx.compose.material.icons.outlined.DirectionsSubway
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

sealed class BusTimetablesBottomNavigationScreens(val route: String,
                                                  @StringRes val resourceId: Int) {
    object Bus : BottomNavigationScreens(
        route = "Weekdays",
        resourceId = R.string.title_weekdays)
    object TrolleyBus : BottomNavigationScreens(
        route = "Saturday",
        resourceId = R.string.title_saturday)
    object MidiBus : BottomNavigationScreens(
        route = "Sunday",
        resourceId = R.string.sunday_dest)
}

@Composable
fun BusTimetablesScreen() {
    val navController = rememberNavController()

    val bottomNavigationItems = listOf(
        BottomNavigationScreens.Bus,
        BottomNavigationScreens.TrolleyBus,
        BottomNavigationScreens.MidiBus,
    )

    Scaffold(
        bottomBar = {
            BusTimetablesBottomNavigation(navController,
                                          bottomNavigationItems)
        }
    ) {
        BusTimetablesNavigationConfigurations(navController)
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
                icon = { Icon(screen.icon) },
                label = { Text(text = stringResource(id = screen.resourceId)) },
                selected = currentRoute == screen.route,
                onClick = {
                    // This is the equivalent to popUpTo the start destination
                    //navController.popBackStack(navController.graph.startDestination, false)
                    navController.popBackStack()

                    // This if check gives us a "singleTop" behavior where we do not create a
                    // second instance of the composable if we are already on that destination
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route)
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