package com.example.diamondguesthouse.presentation.home_screen

import androidx.lifecycle.viewModelScope
import com.example.diamondguesthouse.core.utils.GenericViewModel
import com.example.diamondguesthouse.domain.models.RoomWithCustomersModel
import com.example.diamondguesthouse.domain.repo.RoomRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import com.example.diamondguesthouse.appNavigation.GuestHouseNavKey
import org.koin.core.annotation.KoinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

@KoinViewModel
class HomeScreenViewModel(
    private val roomRepository: RoomRepository,
) : GenericViewModel<HomeUserEvent, HomeUiEvent>() {

    val roomsWithCustomers = roomRepository.getAllRoomsWithCustomers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    val monthlyIncome = roomsWithCustomers.map(::computeMonthlyIncome)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "0 Rs.")

    val todayCheckIns = roomsWithCustomers.map(::computeTodayCheckIns)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "0")

    val todayCheckOuts = roomsWithCustomers.map(::computeTodayCheckOuts)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "0")

    override fun onUserEvent(event: HomeUserEvent) {
        when (event) {
            is HomeUserEvent.NavigateTo -> submitUIEvent(HomeUiEvent.Navigate(event.key))
        }
    }

    fun greetingMessage(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }

    private fun computeMonthlyIncome(list: List<RoomWithCustomersModel>): String {
        val today = Calendar.getInstance()
        val currentMonth = today.get(Calendar.MONTH)
        val currentYear = today.get(Calendar.YEAR)
        val monthlyIncome = list
            .filter { rwc ->
                val checkInDate = Calendar.getInstance().apply {
                    timeInMillis = rwc.room.checkInDate
                }
                checkInDate.get(Calendar.MONTH) == currentMonth &&
                    checkInDate.get(Calendar.YEAR) == currentYear
            }
            .sumOf { it.room.roomPrice }
        return "$monthlyIncome Rs."
    }

    private fun computeTodayCheckIns(list: List<RoomWithCustomersModel>): String {
        val today = LocalDate.now()
        val count = list.count {
            val checkInDate = Instant.ofEpochMilli(it.room.checkInDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            checkInDate == today
        }
        return count.toString()
    }

    private fun computeTodayCheckOuts(list: List<RoomWithCustomersModel>): String {
        val today = LocalDate.now()
        val count = list.count {
            val checkOutDate = Instant.ofEpochMilli(it.room.checkOutDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            checkOutDate == today
        }
        return count.toString()
    }
}

sealed interface HomeUserEvent {
    data class NavigateTo(val key: GuestHouseNavKey) : HomeUserEvent
}

sealed interface HomeUiEvent {
    data class Navigate(val key: GuestHouseNavKey) : HomeUiEvent
}
