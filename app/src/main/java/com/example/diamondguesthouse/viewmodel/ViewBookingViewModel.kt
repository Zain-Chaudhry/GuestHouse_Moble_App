package com.example.diamondguesthouse.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.diamondguesthouse.data.GuestHouseDatabase
import com.example.diamondguesthouse.data.dao.RoomsDao
import com.example.diamondguesthouse.data.model.RoomWithCustomers
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ViewBookingViewModel(dao: RoomsDao): ViewModel() {

    val roomsWithCustomers = dao.getAllRoomsWithCustomers().asLiveData()

    // Function to get today's check-ins
    fun getTodayCheckIns(roomsWithCustomers: List<RoomWithCustomers>): List<RoomWithCustomers> {
        val today = getTodayDate()

        // Filter the list of rooms based on today's date
        return roomsWithCustomers.filter { roomWithCustomers ->
            val checkInDate = Date(roomWithCustomers.room.checkInDate)
            val formattedCheckInDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(checkInDate)

            formattedCheckInDate == today
        }  // Return empty list if no check-ins for today
    }

    fun getTodayCheckOuts(roomsWithCustomers: List<RoomWithCustomers>): List<RoomWithCustomers> {
        val today = getTodayDate()

        // Filter the list of rooms based on today's date
        return roomsWithCustomers.filter { roomWithCustomers ->
            val checkOutDate = Date(roomWithCustomers.room.checkOutDate)
            val formattedCheckInDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(checkOutDate)

            formattedCheckInDate == today
        }  // Return empty list if no check-ins for today
    }

    // Utility function to get today's date as a formatted string
    private fun getTodayDate(): String {
        val today = Calendar.getInstance().time
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today)
    }
}


class ViewBookingViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = GuestHouseDatabase.getDatabase(context).roomDao()
        @Suppress ("UNCHECKED_CAST")
        return ViewBookingViewModel(dao) as T
    }
}
