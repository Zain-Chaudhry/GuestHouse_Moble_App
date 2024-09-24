package com.example.diamondguesthouse

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diamondguesthouse.NavRoutes.BookingBySearch
import com.example.diamondguesthouse.userinterface.screens.AddRecord
import com.example.diamondguesthouse.userinterface.screens.BookingBySearch
import com.example.diamondguesthouse.userinterface.screens.ForgotPasswordScreen
import com.example.diamondguesthouse.userinterface.screens.HomeScreen
import com.example.diamondguesthouse.userinterface.screens.LoginScreen
import com.example.diamondguesthouse.userinterface.screens.SearchRecord
import com.example.diamondguesthouse.userinterface.screens.SignUp
import com.example.diamondguesthouse.userinterface.screens.ViewBooking
import com.example.diamondguesthouse.userinterface.screens.ViewCheckOuts
import com.example.diamondguesthouse.viewmodel.SearchRecordViewModel

object NavRoutes {
    const val Home = "home"
    const val Login = "login"
    const val SignUp = "signup"
    const val ForgotPassword = "forgot_password"
    const val AddRecord = "add_Record"
    const val SearchRecord = "search_Record"
    const val BookingBySearch = "booking_by_search"
    const val ViewBooking = "view_booking"
    const val ViewCheckOut = "view_checkout"

}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val sharedViewModel: SearchRecordViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable(NavRoutes.Login) { LoginScreen(navController) }
        composable(NavRoutes.SignUp) { SignUp(navController) }
        composable(NavRoutes.ForgotPassword) { ForgotPasswordScreen(navController) }
        composable(NavRoutes.Home) { HomeScreen(navController) }
        composable(NavRoutes.AddRecord) { AddRecord(navController) }
        composable(NavRoutes.SearchRecord) { SearchRecord(navController, sharedViewModel) }
        composable(BookingBySearch) { BookingBySearch(sharedViewModel, navController)}
        composable(NavRoutes.ViewBooking) { ViewBooking( navController = navController) }
        composable(NavRoutes.ViewCheckOut) { ViewCheckOuts( navController = navController) }


    }
}