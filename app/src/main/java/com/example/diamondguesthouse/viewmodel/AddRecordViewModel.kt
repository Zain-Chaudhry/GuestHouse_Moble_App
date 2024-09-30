package com.example.diamondguesthouse.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diamondguesthouse.data.dao.CustomerDao
import com.example.diamondguesthouse.data.dao.RoomsDao
import com.example.diamondguesthouse.data.model.CustomerEntity
import com.example.diamondguesthouse.data.model.RoomEntity
import kotlinx.coroutines.launch

class AddRecordViewModel : ViewModel() {

    // for customer status check
    private val _customerStatus = MutableLiveData<CustomerStatus>()
    val customerStatus: LiveData<CustomerStatus> = _customerStatus

    // Room data
    var roomNo: String by mutableStateOf("Please Select")
    var roomPrice: String by mutableStateOf("")
    var checkInDate: Long by mutableLongStateOf(System.currentTimeMillis())
    var checkOutDate: Long by mutableLongStateOf(0L)
    var checkInTime: Long by mutableLongStateOf(System.currentTimeMillis())
    var checkOutTime: Long by mutableLongStateOf(0L)

    // List of customer records
    var customerRecords = mutableStateListOf<CustomerEntityData>()

    // Add a new customer record
    fun addCustomerRecord() {
        customerRecords.add(CustomerEntityData())
    }

    // Remove a customer record by index
    fun removeCustomerRecord(index: Int) {
        if (index >= 0 && index < customerRecords.size) {
            customerRecords.removeAt(index)
        }
    }

    private fun validateCustomerData(): Boolean {
        return customerRecords.all { customer ->
            customer.name.value.isNotBlank() &&
            customer.cellNo.value.isNotBlank() &&
            customer.selectedGender.value.isNotBlank() &&
            customer.permanentAddress.value.isNotBlank() &&
            customer.fatherName.value.isNotBlank()
        }
    }

    private fun validateRoomData(): Boolean {
        return roomNo.isNotBlank() && roomPrice.isNotBlank() && checkInDate > 0 && checkOutDate > 0 && checkInTime > 0 && checkOutTime > 0
    }

    fun submitRecord(roomDao: RoomsDao, customerDao: CustomerDao) {


        _customerStatus.value = CustomerStatus.Loading
        viewModelScope.launch {

            if (!validateRoomData()) {
                _customerStatus.value = CustomerStatus.Error("Incomplete room details")
                return@launch
            }

            if (!validateCustomerData()) {
                _customerStatus.value = CustomerStatus.Error("Incomplete customer data")
                return@launch
            }
            var existingCustomer: CustomerEntity? = null
            for (customer in customerRecords) {
               existingCustomer = customerDao.findActiveCustomerByCnicOrPassport(
                    customer.cnic.value,
                    customer.passportNo.value,
                    System.currentTimeMillis()
                )
            }
            if (existingCustomer != null) {
                _customerStatus.value =
                    CustomerStatus.AlreadyCheckedIn("Customer is already checked in")
                return@launch
            } else {
                _customerStatus.value = CustomerStatus.NewCustomer
                val roomEntity = RoomEntity(
                    roomNo = roomNo,
                    roomPrice = roomPrice.toDouble(),
                    checkInDate = checkInDate,
                    checkOutDate = checkOutDate,
                    checkInTime = checkInTime,
                    checkOutTime = checkOutTime
                )

                // Create a list of CustomerEntities
                val customerEntities = customerRecords.map { customerData ->
                    CustomerEntity(
                        customerId = null,
                        roomNo = roomNo,
                        name = customerData.name.value,
                        fatherName = customerData.fatherName.value,
                        cellNo = customerData.cellNo.value,
                        cnic = customerData.cnic.value,
                        permanentAddress = customerData.permanentAddress.value,
                        selectedGender = customerData.selectedGender.value,
                        country = customerData.country.value,
                        passportNo = customerData.passportNo.value,
                        visaUpTill = customerData.visaUpTill.value,
                        checkInDate = checkInDate,
                        checkOutDate = checkOutDate,
                        checkInTime = checkInTime,
                        checkOutTime = checkOutTime
                    )
                }

                // Insert both room and customers in one transaction
                roomDao.insertRoomWithCustomers(roomEntity, customerEntities)
                _customerStatus.value = CustomerStatus.CustomerAdded("Customer added successfully")


            }
        }
    }

    fun resetCustomerStatus() {
        _customerStatus.value = CustomerStatus.Loading
    }

}

sealed class CustomerStatus {
    data object Loading : CustomerStatus()
    data class AlreadyCheckedIn(val message: String) : CustomerStatus()
    data object NewCustomer : CustomerStatus()
    data class CustomerAdded(val message: String) : CustomerStatus()
    data class Error(val message: String) : CustomerStatus()
}

data class CustomerEntityData(
    var name: MutableState<String> = mutableStateOf(""),
    var fatherName: MutableState<String> = mutableStateOf(""),
    var cellNo: MutableState<String> = mutableStateOf(""),
    var cnic: MutableState<String?> = mutableStateOf(null),
    var permanentAddress: MutableState<String> = mutableStateOf(""),
    var selectedGender: MutableState<String> = mutableStateOf("Please Select"),
    var country: MutableState<String?> = mutableStateOf(""),
    var passportNo: MutableState<String?> = mutableStateOf(null),
    var visaUpTill: MutableState<Long?> = mutableStateOf(0L)
)
