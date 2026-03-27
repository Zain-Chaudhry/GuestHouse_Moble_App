package com.example.diamondguesthouse.presentation.view_check_outs_screen

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

class ViewCheckOutsScreenViewModel(
    roomRepository: RoomRepository,
) : GenericViewModel<ViewCheckOutsUserEvent, ViewCheckOutsUiEvent>() {

    private val roomsWithCustomers = roomRepository.getAllRoomsWithCustomers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    val todayCheckOuts = roomsWithCustomers.map(::filterTodayCheckOuts)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    override fun onUserEvent(event: ViewCheckOutsUserEvent) {}
}

sealed interface ViewCheckOutsUserEvent

sealed interface ViewCheckOutsUiEvent

private fun filterTodayCheckOuts(roomsWithCustomers: List<RoomWithCustomersModel>): List<RoomWithCustomersModel> {
    val today = getTodayDate()
    return roomsWithCustomers.filter { rwc ->
        val formatted = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date(rwc.room.checkOutDate))
        formatted == today
    }
}

private fun getTodayDate(): String {
    val today = Calendar.getInstance().time
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today)
}
