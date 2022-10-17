package com.example.ratbvkotlin.ui.common.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ratbvkotlin.ui.common.BusBottomNavigationScreens

@Composable
fun BusBottomNavigationComponent(
    navHostController: NavHostController,
    bottomNavigationTabs: List<BusBottomNavigationScreens>
) {
    BottomNavigation {
        val currentRoute = currentRoute(navHostController)

        bottomNavigationTabs.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    screen.iconResourceId?.let { iconResourceId ->
                        Icon(
                            painter = painterResource(id = iconResourceId),
                            contentDescription = "Bus Type",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(
                            id = screen.titleResourceId
                        )
                    )
                },
                selected = currentRoute == screen.route,
                onClick = {
                    // This is the equivalent to popUpTo the start destination
                    //navController.popBackStack(navController.graph.startDestination, false)
                    navHostController.popBackStack()

                    // This if check gives us a "singleTop" behavior where we do not create a
                    // second instance of the composable if we are already on that destination
                    if (currentRoute != screen.route) {
                        navHostController.navigate(screen.route)
                    }
                }
            )
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}