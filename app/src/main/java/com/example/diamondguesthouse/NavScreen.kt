package com.example.diamondguesthouse

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diamondguesthouse.userinterface.screens.AddRecord
import com.example.diamondguesthouse.userinterface.screens.BookingBySearch
import com.example.diamondguesthouse.userinterface.screens.ForgotPasswordScreen
import com.example.diamondguesthouse.userinterface.screens.HomeScreen
import com.example.diamondguesthouse.userinterface.screens.LoginScreen
import com.example.diamondguesthouse.userinterface.screens.ReportScreen
import com.example.diamondguesthouse.userinterface.screens.SearchRecord
import com.example.diamondguesthouse.userinterface.screens.SettingScreen
import com.example.diamondguesthouse.userinterface.screens.SignUp
import com.example.diamondguesthouse.userinterface.screens.ViewBooking
import com.example.diamondguesthouse.userinterface.screens.ViewCheckOuts
import com.example.diamondguesthouse.viewmodel.AddRecordViewModel
import com.example.diamondguesthouse.viewmodel.AuthViewModel
import com.example.diamondguesthouse.viewmodel.SearchRecordViewModel

object NavRoutes {
    const val HOME = "home"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val FORGOT_PASSWORD = "forgot_password"
    const val ADD_RECORD = "add_Record"
    const val SEARCH_RECORD = "search_Record"
    const val BOOKING_BY_SEARCH = "booking_by_search"
    const val VIEW_BOOKING = "view_booking"
    const val VIEW_CHECKOUT = "view_checkout"
    const val SETTING_SCREEN = "settings_screen"
    const val REPORT_SCREEN = "report_screen"

}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val sharedViewModel: SearchRecordViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val addRecordViewModel: AddRecordViewModel = viewModel()

    NavHost(navController = navController, startDestination = NavRoutes.LOGIN) {
        composable(NavRoutes.LOGIN) { LoginScreen(authViewModel, navController) }
        composable(NavRoutes.SIGNUP) { SignUp(authViewModel, navController) }
        composable(NavRoutes.FORGOT_PASSWORD) { ForgotPasswordScreen(authViewModel) }
        composable(NavRoutes.HOME) { HomeScreen(navController) }
        composable(NavRoutes.ADD_RECORD) { AddRecord(addRecordViewModel, navController) }
        composable(NavRoutes.SEARCH_RECORD) { SearchRecord(navController, sharedViewModel) }
        composable(NavRoutes.BOOKING_BY_SEARCH) { BookingBySearch(sharedViewModel, navController)}
        composable(NavRoutes.VIEW_BOOKING) { ViewBooking( navController = navController) }
        composable(NavRoutes.VIEW_CHECKOUT) { ViewCheckOuts( navController = navController) }
        composable(NavRoutes.SETTING_SCREEN) { SettingScreen(authViewModel, navController) }
        composable(NavRoutes.REPORT_SCREEN) { ReportScreen(navController) }
    }
}
