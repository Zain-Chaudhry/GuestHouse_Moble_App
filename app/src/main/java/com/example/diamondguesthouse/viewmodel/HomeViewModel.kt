package com.example.diamondguesthouse.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diamondguesthouse.data.GuestHouseDatabase
import com.example.diamondguesthouse.data.dao.RoomsDao
import com.example.diamondguesthouse.data.model.RoomWithCustomers
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

class HomeViewModel(dao: RoomsDao) : ViewModel() {
    val roomsWithCustomers = dao.getAllRoomsWithCustomers()

    fun getMonthlyIncome(list: List<RoomWithCustomers>): String {
        val today = Calendar.getInstance()
        val currentMonth = today.get(Calendar.MONTH)
        val currentYear = today.get(Calendar.YEAR)

        val monthlyIncome = list
            .filter { roomWithCustomers ->
                val checkInDate = Calendar.getInstance().apply {
                    timeInMillis = roomWithCustomers.room.checkInDate
                }
                checkInDate.get(Calendar.MONTH) == currentMonth && checkInDate.get(Calendar.YEAR) == currentYear
            }
            .sumOf { it.room.roomPrice }

        return "$monthlyIncome Rs."
    }

    fun getCheckIns(list: List<RoomWithCustomers>): String {
        val today = LocalDate.now()

        val count = list.count {
            val checkInDate = Instant.ofEpochMilli(it.room.checkInDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            checkInDate == today
        }
        return count.toString()
    }

    fun getCheckOuts(list: List<RoomWithCustomers>): String {
        val today = LocalDate.now()

        val count = list.count {
            val checkOutDate = Instant.ofEpochMilli(it.room.checkOutDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            checkOutDate == today
        }
        return count.toString()
    }

    // Generate a greeting message based on the time of day
    fun getGreetingMessage(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }
}



class HomeViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = GuestHouseDatabase.getDatabase(context).roomDao()
        @Suppress ("UNCHECKED_CAST")
        return HomeViewModel(dao) as T
    }
}
