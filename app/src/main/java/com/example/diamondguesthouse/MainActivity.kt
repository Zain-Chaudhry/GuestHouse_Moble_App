package com.example.diamondguesthouse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diamondguesthouse.ui.theme.DiamondGuestHouseTheme
import com.example.diamondguesthouse.userinterface.screens.AddRecord
import com.example.diamondguesthouse.userinterface.screens.ForgotPasswordScreen
import com.example.diamondguesthouse.userinterface.screens.HomeScreen
import com.example.diamondguesthouse.userinterface.screens.LoginScreen
import com.example.diamondguesthouse.userinterface.screens.SignUp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiamondGuestHouseTheme {
                AppNavigation()
//                   HomeScreen()

                }
            }
        }
    }

object NavRoutes {
    const val Home = "home"
    const val Login = "login"
    const val SignUp = "signup"
    const val ForgotPassword = "forgot_password"
    const val AddRecord = "add_Record"

}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable(NavRoutes.Login) { LoginScreen(navController) }
        composable(NavRoutes.SignUp) { SignUp(navController) }
        composable(NavRoutes.ForgotPassword) { ForgotPasswordScreen(navController)}
        composable(NavRoutes.Home) { HomeScreen(navController) }
        composable(NavRoutes.AddRecord) { AddRecord(navController)}
    }
}