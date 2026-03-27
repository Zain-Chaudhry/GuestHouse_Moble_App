package com.example.diamondguesthouse.presentation.view_booking_screen

import androidx.lifecycle.viewModelScope
import com.example.diamondguesthouse.core.utils.GenericViewModel
import com.example.diamondguesthouse.domain.models.RoomWithCustomersModel
import com.example.diamondguesthouse.domain.repo.RoomRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ViewBookingScreenViewModel(
    roomRepository: RoomRepository,
) : GenericViewModel<ViewBookingUserEvent, ViewBookingUiEvent>() {

    private val roomsWithCustomers = roomRepository.getAllRoomsWithCustomers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    val todayCheckIns = roomsWithCustomers.map(::filterTodayCheckIns)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    override fun onUserEvent(event: ViewBookingUserEvent) {}
}

sealed interface ViewBookingUserEvent

sealed interface ViewBookingUiEvent

private fun filterTodayCheckIns(roomsWithCustomers: List<RoomWithCustomersModel>): List<RoomWithCustomersModel> {
    val today = getTodayDate()
    return roomsWithCustomers.filter { rwc ->
        val formatted = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date(rwc.room.checkInDate))
        formatted == today
    }
}

private fun getTodayDate(): String {
    val today = Calendar.getInstance().time
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today)
}
