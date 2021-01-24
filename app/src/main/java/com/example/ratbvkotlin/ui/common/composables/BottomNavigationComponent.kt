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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
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
                            imageVector = vectorResource(id = iconResourceId),
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
                selected = currentRoute == screen.type,
                onClick = {
                    // This is the equivalent to popUpTo the start destination
                    //navController.popBackStack(navController.graph.startDestination, false)
                    navHostController.popBackStack()

                    // This if check gives us a "singleTop" behavior where we do not create a
                    // second instance of the composable if we are already on that destination
                    if (currentRoute != screen.type) {
                        navHostController.navigate(screen.type)
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