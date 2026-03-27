package com.example.diamondguesthouse.appNavigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface GuestHouseNavKey : NavKey {

    @Serializable
    @SerialName("login")
    data object Login : GuestHouseNavKey

    @Serializable
    @SerialName("sign_up")
    data object SignUp : GuestHouseNavKey

    @Serializable
    @SerialName("forgot_password")
    data object ForgotPassword : GuestHouseNavKey

    @Serializable
    @SerialName("home")
    data object Home : GuestHouseNavKey

    @Serializable
    @SerialName("add_record")
    data object AddRecord : GuestHouseNavKey

    @Serializable
    @SerialName("search_record")
    data object SearchRecord : GuestHouseNavKey

    @Serializable
    @SerialName("booking_by_search")
    data object BookingBySearch : GuestHouseNavKey

    @Serializable
    @SerialName("view_booking")
    data object ViewBooking : GuestHouseNavKey

    @Serializable
    @SerialName("view_checkouts")
    data object ViewCheckOuts : GuestHouseNavKey

    @Serializable
    @SerialName("settings")
    data object Settings : GuestHouseNavKey

    @Serializable
    @SerialName("report")
    data object Report : GuestHouseNavKey
}
