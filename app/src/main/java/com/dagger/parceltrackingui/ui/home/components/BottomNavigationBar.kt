package com.dagger.parceltrackingui.ui.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector

object ParcelRoute {
    const val HOME = "Home"
    const val PACKAGES = "Packages"
    const val PAYMENT = "Payment"
    const val SETTINGS = "Settings"
}

data class MainNavigationDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String
)

val TOP_LEVEL_DESTINATIONS = listOf(
    MainNavigationDestination(
        route = ParcelRoute.HOME,
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Rounded.Home,
        iconText = "Home"
    ),
    MainNavigationDestination(
        route = ParcelRoute.PACKAGES,
        selectedIcon = Icons.Default.AccountBox,
        unselectedIcon = Icons.Default.AccountBox,
        iconText = "Packages"
    ),
    MainNavigationDestination(
        route = ParcelRoute.PAYMENT,
        selectedIcon = Icons.Outlined.Phone,
        unselectedIcon = Icons.Outlined.Phone,
        iconText = "Payment"
    ),
    MainNavigationDestination(
        route = ParcelRoute.SETTINGS,
        selectedIcon = Icons.Default.Settings,
        unselectedIcon = Icons.Default.Settings,
        iconText = "Settings"
    )
)