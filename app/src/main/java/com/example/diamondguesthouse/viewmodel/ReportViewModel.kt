package com.example.diamondguesthouse.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diamondguesthouse.data.GuestHouseDatabase
import com.example.diamondguesthouse.data.dao.RoomsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class ReportViewModel (
    private val dao: RoomsDao
): ViewModel() {

    // Mutable state to hold the selected report type
    var reportType: String by mutableStateOf("Please Select")

    // Mutable states for report results
    var totalCheckIns by mutableIntStateOf(0)
    var totalIncome by mutableDoubleStateOf(0.0)

    // Function to fetch reports based on the selected type
    fun fetchReport() {
        // Launch a coroutine to fetch report data
        CoroutineScope(Dispatchers.IO).launch {
            val today = LocalDate.now()
            when (reportType) {
                "Daily Report" -> {
                    val startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    val endOfDay = startOfDay + (24 * 60 * 60 * 1000) // Add 1 day in milliseconds
                    totalCheckIns = dao.getCountOfRoomsBookedBetween(startOfDay, endOfDay)
                    totalIncome = dao.getTotalIncomeBetweenDates(startOfDay, endOfDay)?: 0.0
                }
                "Weekly Report" -> {
                    val startOfWeek = today.minusDays(today.dayOfWeek.value.toLong()).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    val endOfWeek = startOfWeek + (7 * 24 * 60 * 60 * 1000) // Add 7 days in milliseconds
                    totalCheckIns = dao.getCountOfRoomsBookedBetween(startOfWeek, endOfWeek)
                    totalIncome = dao.getTotalIncomeBetweenDates(startOfWeek, endOfWeek)?: 0.0
                }
                "Monthly Report" -> {
                    val startOfMonth = today.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    val endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() + (24 * 60 * 60 * 1000) // Add 1 day in milliseconds
                    totalCheckIns = dao.getCountOfRoomsBookedBetween(startOfMonth, endOfMonth)
                    totalIncome = dao.getTotalIncomeBetweenDates(startOfMonth, endOfMonth)?: 0.0
                }
                else -> {
                    totalCheckIns = 0
                    totalIncome = 0.0
                }
            }
        }
    }


}

class ReportViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = GuestHouseDatabase.getDatabase(context).roomDao()
        @Suppress ("UNCHECKED_CAST")
        return ReportViewModel(dao) as T
    }
}
