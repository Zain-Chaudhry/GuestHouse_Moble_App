package com.example.diamondguesthouse.presentation.report_screen

import androidx.lifecycle.viewModelScope
import com.example.diamondguesthouse.core.utils.GenericViewModel
import com.example.diamondguesthouse.domain.repo.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

data class ReportUiState(
    val reportType: String = "Please Select",
    val totalCheckIns: Int = 0,
    val totalIncome: Double = 0.0,
)

sealed interface ReportUserEvent {
    data class ReportTypeChanged(val value: String) : ReportUserEvent
    data object FetchReport : ReportUserEvent
}

sealed interface ReportUiEvent

class ReportScreenViewModel(
    private val roomRepository: RoomRepository,
) : GenericViewModel<ReportUserEvent, ReportUiEvent>() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState = _uiState.asStateFlow()

    override fun onUserEvent(event: ReportUserEvent) {
        when (event) {
            is ReportUserEvent.ReportTypeChanged -> {
                _uiState.update { it.copy(reportType = event.value) }
                fetchReport()
            }
            ReportUserEvent.FetchReport -> fetchReport()
        }
    }

    private fun fetchReport() {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val type = _uiState.value.reportType
            val today = LocalDate.now()
            when (type) {
                "Daily Report" -> {
                    val start = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    val end = start + (24 * 60 * 60 * 1000)
                    applyRange(start, end)
                }
                "Weekly Report" -> {
                    val start = today.minusDays(today.dayOfWeek.value.toLong())
                        .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    val end = start + (7 * 24 * 60 * 60 * 1000)
                    applyRange(start, end)
                }
                "Monthly Report" -> {
                    val start = today.withDayOfMonth(1)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    val end = today.withDayOfMonth(today.lengthOfMonth())
                        .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() + (24 * 60 * 60 * 1000)
                    applyRange(start, end)
                }
                else -> _uiState.update { it.copy(totalCheckIns = 0, totalIncome = 0.0) }
            }
        }
    }

    private suspend fun applyRange(start: Long, end: Long) {
        val ins = roomRepository.getBookedCountBetween(start, end)
        val income = roomRepository.getTotalIncomeBetween(start, end)
        _uiState.update { it.copy(totalCheckIns = ins, totalIncome = income) }
    }
}
