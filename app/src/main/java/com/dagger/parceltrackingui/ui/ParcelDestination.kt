package com.dagger.parceltrackingui.ui

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface ParcelDestination {
    val route: String
}

object Home: ParcelDestination {
    override val route: String
        get() = "home"
}

object ParcelDetail: ParcelDestination {
    override val route: String
        get() = "detail_parcel"
    const val argId = "id"
    const val argParcelType = "parcel_type"
    const val argEnableChat = "enable_chat"
    val routeWithArgs = "$route/{$argParcelType}/{$argId}?$argEnableChat={$argEnableChat}"

    val arguments = listOf(
        navArgument(argId) { type = NavType.StringType },
        navArgument(argParcelType) { type = NavType.StringType },
        navArgument(argEnableChat) { type = NavType.BoolType; defaultValue = true}
    )

    fun getRouteWithArgs(parcelId: String, parcelType: String, enableChat: Boolean) =
        "$route/$parcelType/$parcelId?$argEnableChat=$enableChat"
}