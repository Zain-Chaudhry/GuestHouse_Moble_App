package com.example.diamondguesthouse.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diamondguesthouse.data.GuestHouseDatabase
import com.example.diamondguesthouse.data.dao.CustomerDao
import com.example.diamondguesthouse.data.model.CustomerEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

class HomeViewModel(dao: CustomerDao): ViewModel() {

    val customers = dao.getAllCustomers()

    fun getMonthlyIncome(list: List<CustomerEntity>): String {
        val today = Calendar.getInstance()
        val currentMonth = today.get(Calendar.MONTH)
        val currentYear = today.get(Calendar.YEAR)

        val monthlyIncome = list
            .filter { customer ->
                val checkInDate = Calendar.getInstance().apply {
                    timeInMillis = customer.checkInDate
                }
                checkInDate.get(Calendar.MONTH) == currentMonth && checkInDate.get(Calendar.YEAR) == currentYear
            }
            .sumOf { it.roomPrice }

        return "$monthlyIncome Rs."
    }

    fun getCheckIns(list: List<CustomerEntity>): String {
        val today = LocalDate.now()

        val count = list.count {
            val checkInDate = Instant.ofEpochMilli(it.checkInDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            checkInDate == today
        }
        return count.toString()
    }

    fun getCheckOuts(list: List<CustomerEntity>): String {
        val today = LocalDate.now()

        val count = list.count {
            val checkOutDate = Instant.ofEpochMilli(it.checkOutDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            checkOutDate == today
        }
        return count.toString()
    }
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
        val dao = GuestHouseDatabase.getDatabase(context).customerDao()
        @Suppress ("UNCHECKED_CAST")
        return HomeViewModel(dao) as T
    }
}
