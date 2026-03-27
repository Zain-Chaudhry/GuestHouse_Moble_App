package com.example.diamondguesthouse.appNavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.diamondguesthouse.presentation.add_record_screen.AddRecordScreen
import com.example.diamondguesthouse.presentation.booking_by_search_screen.BookingBySearchScreen
import com.example.diamondguesthouse.presentation.forgot_password_screen.ForgotPasswordScreen
import com.example.diamondguesthouse.presentation.home_screen.HomeScreen
import com.example.diamondguesthouse.presentation.login_screen.LoginScreen
import com.example.diamondguesthouse.presentation.report_screen.ReportScreen
import com.example.diamondguesthouse.presentation.search_record_screen.SearchRecordScreen
import com.example.diamondguesthouse.presentation.setting_screen.SettingScreen
import com.example.diamondguesthouse.presentation.sign_up_screen.SignUpScreen
import com.example.diamondguesthouse.presentation.view_booking_screen.ViewBookingScreen
import com.example.diamondguesthouse.presentation.view_check_outs_screen.ViewCheckOutsScreen

@Composable
fun GuestHouseAppNavigation() {
    val backStack = remember {
        mutableStateListOf<GuestHouseNavKey>(GuestHouseNavKey.Login)
    }
    val onNavigate: OnGuestHouseNavigate = remember(backStack) {
        { cmd -> backStack.applyNavCommand(cmd) }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            navEntry(key, onNavigate)
        },
    )
}

private fun navEntry(
    key: GuestHouseNavKey,
    onNavigate: OnGuestHouseNavigate,
): NavEntry<GuestHouseNavKey> = when (key) {
    GuestHouseNavKey.Login -> NavEntry(key) {
        LoginScreen(onNavigate = onNavigate)
    }
    GuestHouseNavKey.SignUp -> NavEntry(key) {
        SignUpScreen(onNavigate = onNavigate)
    }
    GuestHouseNavKey.ForgotPassword -> NavEntry(key) {
        ForgotPasswordScreen(onNavigate = onNavigate)
    }
    GuestHouseNavKey.Home -> NavEntry(key) {
        HomeScreen(onNavigate = onNavigate)
    }
    GuestHouseNavKey.AddRecord -> NavEntry(key) {
        AddRecordScreen(onNavigate = onNavigate)
    }
    GuestHouseNavKey.SearchRecord -> NavEntry(key) {
        SearchRecordScreen(onNavigate = onNavigate)
    }
    GuestHouseNavKey.BookingBySearch -> NavEntry(key) {
        BookingBySearchScreen(onNavigate = onNavigate)
    }
    GuestHouseNavKey.ViewBooking -> NavEntry(key) {
        ViewBookingScreen(onNavigate = onNavigate)
    }
    GuestHouseNavKey.ViewCheckOuts -> NavEntry(key) {
        ViewCheckOutsScreen(onNavigate = onNavigate)
    }
    GuestHouseNavKey.Settings -> NavEntry(key) {
        SettingScreen(onNavigate = onNavigate)
    }
    GuestHouseNavKey.Report -> NavEntry(key) {
        ReportScreen(onNavigate = onNavigate)
    }
}
