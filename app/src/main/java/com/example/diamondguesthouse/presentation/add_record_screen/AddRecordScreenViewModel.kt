package com.example.diamondguesthouse.presentation.add_record_screen

import androidx.lifecycle.viewModelScope
import com.example.diamondguesthouse.core.utils.GenericViewModel
import com.example.diamondguesthouse.domain.models.CustomerDraftModel
import com.example.diamondguesthouse.domain.models.CustomerModel
import com.example.diamondguesthouse.domain.models.RoomModel
import com.example.diamondguesthouse.domain.repo.CustomerRepository
import com.example.diamondguesthouse.domain.repo.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddRecordUiState(
    val roomNo: String = "Please Select",
    val roomPrice: String = "",
    val checkInDate: Long = System.currentTimeMillis(),
    val checkOutDate: Long = 0L,
    val checkInTime: Long = System.currentTimeMillis(),
    val checkOutTime: Long = 0L,
    val customerRows: List<CustomerDraftModel> = emptyList(),
    val customerStatus: CustomerSubmitStatus = CustomerSubmitStatus.Idle,
)

sealed interface CustomerSubmitStatus {
    data object Idle : CustomerSubmitStatus
    data object Loading : CustomerSubmitStatus
    data class AlreadyCheckedIn(val message: String) : CustomerSubmitStatus
    data object NewCustomer : CustomerSubmitStatus
    data class CustomerAdded(val message: String) : CustomerSubmitStatus
    data class Error(val message: String) : CustomerSubmitStatus
}

sealed interface AddRecordUserEvent {
    data class RoomNoChanged(val value: String) : AddRecordUserEvent
    data class RoomPriceChanged(val value: String) : AddRecordUserEvent
    data class CheckOutDateChanged(val value: Long) : AddRecordUserEvent
    data class CheckOutTimeChanged(val value: Long) : AddRecordUserEvent
    data object AddCustomerRow : AddRecordUserEvent
    data class RemoveCustomerRow(val index: Int) : AddRecordUserEvent
    data class UpdateCustomerDraft(val index: Int, val draft: CustomerDraftModel) : AddRecordUserEvent
    data object SubmitRecord : AddRecordUserEvent
    data object ResetCustomerStatus : AddRecordUserEvent
}

sealed interface AddRecordUiEvent {
    data object NavigateHome : AddRecordUiEvent
    data class Message(val text: String) : AddRecordUiEvent
}

class AddRecordScreenViewModel(
    private val roomRepository: RoomRepository,
    private val customerRepository: CustomerRepository,
) : GenericViewModel<AddRecordUserEvent, AddRecordUiEvent>() {

    private val _uiState = MutableStateFlow(AddRecordUiState())
    val uiState = _uiState.asStateFlow()

    override fun onUserEvent(event: AddRecordUserEvent) {
        when (event) {
            is AddRecordUserEvent.RoomNoChanged -> _uiState.update { it.copy(roomNo = event.value) }
            is AddRecordUserEvent.RoomPriceChanged -> _uiState.update { it.copy(roomPrice = event.value) }
            is AddRecordUserEvent.CheckOutDateChanged -> _uiState.update { it.copy(checkOutDate = event.value) }
            is AddRecordUserEvent.CheckOutTimeChanged -> _uiState.update { it.copy(checkOutTime = event.value) }
            AddRecordUserEvent.AddCustomerRow -> _uiState.update {
                it.copy(customerRows = it.customerRows + CustomerDraftModel())
            }
            is AddRecordUserEvent.RemoveCustomerRow -> _uiState.update { s ->
                val list = s.customerRows.toMutableList()
                if (event.index in list.indices) list.removeAt(event.index)
                s.copy(customerRows = list)
            }
            is AddRecordUserEvent.UpdateCustomerDraft -> _uiState.update { s ->
                val list = s.customerRows.toMutableList()
                if (event.index in list.indices) list[event.index] = event.draft
                s.copy(customerRows = list)
            }
            AddRecordUserEvent.SubmitRecord -> submitRecord()
            AddRecordUserEvent.ResetCustomerStatus -> _uiState.update {
                it.copy(customerStatus = CustomerSubmitStatus.Idle)
            }
        }
    }

    private fun validateCustomerData(rows: List<CustomerDraftModel>): Boolean =
        rows.all { c ->
            c.name.isNotBlank() &&
                c.cellNo.isNotBlank() &&
                c.selectedGender.isNotBlank() &&
                c.permanentAddress.isNotBlank() &&
                c.fatherName.isNotBlank()
        }

    private fun validateRoomData(s: AddRecordUiState): Boolean =
        s.roomNo.isNotBlank() &&
            s.roomPrice.isNotBlank() &&
            s.checkInDate > 0 &&
            s.checkOutDate > 0 &&
            s.checkInTime > 0 &&
            s.checkOutTime > 0

    private fun submitRecord() {
        viewModelScope.launch {
            val s = _uiState.value
            _uiState.update { it.copy(customerStatus = CustomerSubmitStatus.Loading) }
            if (!validateRoomData(s)) {
                _uiState.update { it.copy(customerStatus = CustomerSubmitStatus.Error("Incomplete room details")) }
                return@launch
            }
            if (!validateCustomerData(s.customerRows)) {
                _uiState.update { it.copy(customerStatus = CustomerSubmitStatus.Error("Incomplete customer data")) }
                return@launch
            }
            val now = System.currentTimeMillis()
            var existing: CustomerModel? = null
            for (c in s.customerRows) {
                existing = customerRepository.findActiveCustomerByCnicOrPassport(
                    c.cnic,
                    c.passportNo,
                    now,
                )
                if (existing != null) break
            }
            if (existing != null) {
                _uiState.update {
                    it.copy(customerStatus = CustomerSubmitStatus.AlreadyCheckedIn("Customer is already checked in"))
                }
                return@launch
            }
            val room = RoomModel(
                roomNo = s.roomNo,
                roomPrice = s.roomPrice.toDouble(),
                checkInDate = s.checkInDate,
                checkOutDate = s.checkOutDate,
                checkInTime = s.checkInTime,
                checkOutTime = s.checkOutTime,
            )
            val entities = s.customerRows.map { c ->
                CustomerModel(
                    customerId = null,
                    roomNo = s.roomNo,
                    name = c.name,
                    fatherName = c.fatherName,
                    cellNo = c.cellNo,
                    cnic = c.cnic,
                    permanentAddress = c.permanentAddress,
                    selectedGender = c.selectedGender,
                    country = c.country,
                    passportNo = c.passportNo,
                    visaUpTill = c.visaUpTill,
                    checkInDate = s.checkInDate,
                    checkOutDate = s.checkOutDate,
                    checkInTime = s.checkInTime,
                    checkOutTime = s.checkOutTime,
                )
            }
            roomRepository.insertRoomWithCustomers(room, entities)
            _uiState.update {
                it.copy(customerStatus = CustomerSubmitStatus.CustomerAdded("Customer added successfully"))
            }
        }
    }
}
