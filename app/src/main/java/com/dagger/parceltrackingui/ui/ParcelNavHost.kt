package com.dagger.parceltrackingui.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dagger.parceltrackingui.ui.home.HomeScreen
import com.dagger.parceltrackingui.ui.parcel_detail.ParcelDetailScreen

@Composable
fun ParcelNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Home.route) {
        composable(
            route = Home.route
        ) {
            HomeScreen(
                onNavigateToDetail = { parcelId, parcelType, enableChat ->
                    navController.navigate(
                        ParcelDetail.getRouteWithArgs(parcelId, parcelType, enableChat),
                    ) {
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(ParcelDetail.routeWithArgs, arguments = ParcelDetail.arguments) { navBackStackEntry ->
            val parcelId = navBackStackEntry.arguments?.getString(ParcelDetail.argId) ?: ""
            val parcelType = navBackStackEntry.arguments?.getString(ParcelDetail.argParcelType) ?: ""
            val enableChat = navBackStackEntry.arguments?.getBoolean(ParcelDetail.argEnableChat) ?: true
            ParcelDetailScreen(
                parcelId = parcelId,
                parcelType = parcelType,
                enableChat = enableChat,
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }