package com.example.diamondguesthouse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.diamondguesthouse.ui.theme.DiamondGuestHouseTheme
import com.example.diamondguesthouse.viewmodel.AuthViewModel


class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                !viewModel.isReady.value

            }
        }
        setContent {
            DiamondGuestHouseTheme {
               AppNavigation()
                }
            }
        }
    }

