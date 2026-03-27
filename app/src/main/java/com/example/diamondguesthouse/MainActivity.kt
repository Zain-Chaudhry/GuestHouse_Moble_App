package com.example.diamondguesthouse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.diamondguesthouse.appNavigation.GuestHouseAppNavigation
import com.example.diamondguesthouse.core.utils.InitialSplash
import com.example.diamondguesthouse.ui.theme.DiamondGuestHouseTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val initialSplash: InitialSplash by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().setKeepOnScreenCondition {
            !initialSplash.isReady.value
        }
        setContent {
            DiamondGuestHouseTheme {
                GuestHouseAppNavigation()
            }
        }
    }
}
