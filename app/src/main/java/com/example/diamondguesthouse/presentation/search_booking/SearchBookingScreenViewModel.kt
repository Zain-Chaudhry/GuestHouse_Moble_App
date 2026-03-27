package com.example.diamondguesthouse.presentation.search_booking

import androidx.lifecycle.viewModelScope
import com.example.diamondguesthouse.core.utils.GenericViewModel
import com.example.diamondguesthouse.domain.models.CustomerModel
import com.example.diamondguesthouse.domain.models.RoomModel
import com.example.diamondguesthouse.domain.repo.CustomerRepository
import com.example.diamondguesthouse.domain.repo.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchBookingUiState(
    val roomNo: String = "Please Select",
    val roomPrice: String = "",
    val checkInDate: Long = System.currentTimeMillis(),
    val checkOutDate: Long = 0L,
    val checkInTime: Long = System.currentTimeMillis(),
    val checkOutTime: Long = 0L,
    val cnic: String = "",
    val passportNo: String = "",
    val searchResults: List<CustomerModel> = emptyList(),
    val searchError: String = "",
    val selectedCustomers: List<CustomerModel> = emptyList(),
    val bookingError: String = "",
)

sealed interface SearchBookingUserEvent {
    data class RoomNoChanged(val value: String) : SearchBookingUserEvent
    data class RoomPriceChanged(val value: String) : SearchBookingUserEvent
    data class CheckOutDateChanged(val value: Long) : SearchBookingUserEvent
    data class CheckOutTimeChanged(val value: Long) : SearchBookingUserEvent
    data class CnicChanged(val value: String) : SearchBookingUserEvent
    data class PassportChanged(val value: String) : SearchBookingUserEvent
    data object SearchClicked : SearchBookingUserEvent
    data class AddCustomer(val customer: CustomerModel) : SearchBookingUserEvent
    data class RemoveCustomer(val customer: CustomerModel) : SearchBookingUserEvent
    data object ConfirmBookingClicked : SearchBookingUserEvent
    data object ClearBookingError : SearchBookingUserEvent
}

sealed interface SearchBookingUiEvent {
    data object NavigateHome : SearchBookingUiEvent
    data class Message(val text: String) : SearchBookingUiEvent
}

class SearchBookingScreenViewModel(
    private val roomRepository: RoomRepository,
    private val customerRepository: CustomerRepository,
) : GenericViewModel<SearchBookingUserEvent, SearchBookingUiEvent>() {

    private val _uiState = MutableStateFlow(SearchBookingUiState())
    val uiState = _uiState.asStateFlow()

    override fun onUserEvent(event: SearchBookingUserEvent) {
        when (event) {
            is SearchBookingUserEvent.RoomNoChanged -> _uiState.update { it.copy(roomNo = event.value) }
            is SearchBookingUserEvent.RoomPriceChanged -> _uiState.update { it.copy(roomPrice = event.value) }
            is SearchBookingUserEvent.CheckOutDateChanged -> _uiState.update { it.copy(checkOutDate = event.value) }
            is SearchBookingUserEvent.CheckOutTimeChanged -> _uiState.update { it.copy(checkOutTime = event.value) }
            is SearchBookingUserEvent.CnicChanged -> _uiState.update { it.copy(cnic = event.value) }
            is SearchBookingUserEvent.PassportChanged -> _uiState.update { it.copy(passportNo = event.value) }
            SearchBookingUserEvent.SearchClicked -> searchCustomer()
            is SearchBookingUserEvent.AddCustomer -> addCustomer(event.customer)
            is SearchBookingUserEvent.RemoveCustomer -> removeCustomer(event.customer)
            SearchBookingUserEvent.ConfirmBookingClicked -> confirmBooking()
            SearchBookingUserEvent.ClearBookingError -> _uiState.update { it.copy(bookingError = "") }
        }
    }

    private fun searchCustomer() {
        viewModelIOScope {
            val s = _uiState.value
            try {
                val results = customerRepository.getCustomersByCnicOrPassport(
                    s.cnic.ifBlank { null },
                    s.passportNo.ifBlank { null },
                )
                if (results.isNotEmpty()) {
                    _uiState.update { state ->
                        val merged = (state.searchResults + results).distinctBy {
                            listOfNotNull(it.customerId, it.cnic, it.passportNo, it.cellNo).joinToString()
                        }
                        state.copy(searchResults = merged, searchError = "")
                    }
                } else {
                    _uiState.update { it.copy(searchError = "No customers found") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(searchError = "Error occurred: ${e.message}") }
            }
        }
    }

    private fun addCustomer(customer: CustomerModel) {
        _uiState.update { state ->
            if (state.selectedCustomers.contains(customer)) state
            else state.copy(selectedCustomers = state.selectedCustomers + customer)
        }
    }

    private fun removeCustomer(customer: CustomerModel) {
        _uiState.update { state ->
            state.copy(selectedCustomers = state.selectedCustomers.filter { it != customer })
        }
    }

    private fun confirmBooking() {
        viewModelScope.launch {
            val s = _uiState.value
            try {
                val existing = roomRepository.getRoomByNumber(s.roomNo)
                if (existing != null) {
                    submitUIEvent(SearchBookingUiEvent.Message("Room with number ${s.roomNo} already exists."))
                    return@launch
                }
                val room = RoomModel(
                    roomNo = s.roomNo,
                    roomPrice = s.roomPrice.toDoubleOrNull() ?: 0.0,
                    checkInDate = s.checkInDate,
                    checkOutDate = s.checkOutDate,
                    checkInTime = s.checkInTime,
                    checkOutTime = s.checkOutTime,
                )
                val customers = s.selectedCustomers.map { c ->
                    c.copy(
                        roomNo = room.roomNo,
                        checkInDate = s.checkInDate,
                        checkOutDate = s.checkOutDate,
                        checkInTime = s.checkInTime,
                        checkOutTime = s.checkOutTime,
                    )
                }
                roomRepository.insertRoomWithCustomers(room, customers)
                submitUIEvent(SearchBookingUiEvent.NavigateHome)
            } catch (e: Exception) {
                e.printStackTrace()
                submitUIEvent(SearchBookingUiEvent.Message("Error: ${e.message}"))
            }
        }
    }
}
