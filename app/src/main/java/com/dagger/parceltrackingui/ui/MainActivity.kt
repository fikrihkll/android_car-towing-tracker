package com.dagger.parceltrackingui.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.dagger.parceltrackingui.ui.theme.ParcelTrackingUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParcelApp()
        }
    }

}

@Composable
fun ParcelApp() {
    ParcelTrackingUITheme {
        val navController = rememberNavController()
        ParcelNavHost(navController = navController)
    }
}
