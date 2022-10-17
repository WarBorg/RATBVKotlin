package com.example.ratbvkotlin.ui.common.navigationGraphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ratbvkotlin.ui.buslines.composables.BusLinesScreen
import com.example.ratbvkotlin.ui.busstations.composables.BusStationsScreen
import com.example.ratbvkotlin.ui.bustimetables.composables.BusTimetablesScreen
import com.example.ratbvkotlin.viewmodels.BusLinesViewModel
import com.example.ratbvkotlin.viewmodels.BusStationsViewModel
import com.example.ratbvkotlin.viewmodels.BusTimetablesViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.BUS_LINES
    ) {
        composable(route = Graph.BUS_LINES) {
            val viewModel = getViewModel<BusLinesViewModel>()

            val onBusStationClicked: (String, String, Int , String) -> Unit =
                { linkNormalWay, linkReverseWay, busLineId, busLineName ->
                    navController.navigate(
                        "${Graph.BUS_STATIONS}/${linkNormalWay}/${linkReverseWay}/${busLineId}/${busLineName}"
                    )
                }

            BusLinesScreen(
                viewModel,
                onBusStationClicked
            )
        }
        composable(
            route = "${Graph.BUS_STATIONS}/{linkNormalWay}/{linkReverseWay}/{busLineId}/{busLineName}",
            arguments = listOf(
                navArgument("linkNormalWay") {
                    type = NavType.StringType
                },
                navArgument("linkReverseWay") {
                    type = NavType.StringType
                },
                navArgument("busLineId") {
                    type = NavType.IntType
                },
                navArgument("busLineName") {
                    type = NavType.StringType
                },
            )
        ) {
            val linkNormalWay = it.arguments?.getString("linkNormalWay", "")
            val linkReverseWay = it.arguments?.getString("linkReverseWay", "")
            val busLineId = it.arguments?.getInt("busLineId", 0)
            val busLineName = it.arguments?.getString("busLineName", "")

            val viewModel = getViewModel<BusStationsViewModel> {
                parametersOf(
                    linkNormalWay,
                    linkReverseWay,
                    busLineId,
                    busLineName,
                )
            }

            val onBackNavigation: () -> Unit = {
                navController.popBackStack()
            }

            val onBusStationClicked: (String, Int , String) -> Unit =
                { scheduleLink, stationId, stationName ->
                    navController.navigate(
                        "${Graph.BUS_TIMETABLES}/${scheduleLink}/${stationId}/${stationName}"
                    )
                }

            BusStationsScreen(
                viewModel,
                onBackNavigation,
                onBusStationClicked,
            )
        }
        composable(
            route = "${Graph.BUS_TIMETABLES}/{scheduleLink}/{busStationId}/{busStationName}",
            arguments = listOf(
                navArgument("scheduleLink") {
                    type = NavType.StringType
                },
                navArgument("busStationId") {
                    type = NavType.IntType
                },
                navArgument("busStationName") {
                    type = NavType.StringType
                },
            )
        ) {
            val scheduleLink = it.arguments?.getString("scheduleLink", "")
            val busStationId = it.arguments?.getInt("busStationId", 0)
            val busStationName = it.arguments?.getString("busStationName", "")

            val viewModel = getViewModel<BusTimetablesViewModel> {
                parametersOf(
                    scheduleLink,
                    busStationId,
                    busStationName,
                )
            }

            val onBackNavigation: () -> Unit = {
                navController.popBackStack()
            }

            BusTimetablesScreen(
                viewModel,
                onBackNavigation,
            )
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val BUS_LINES = "bus_lines_graph"
    const val BUS_STATIONS = "bus_stations_graph"
    const val BUS_TIMETABLES = "bus_timetables_graph"
}