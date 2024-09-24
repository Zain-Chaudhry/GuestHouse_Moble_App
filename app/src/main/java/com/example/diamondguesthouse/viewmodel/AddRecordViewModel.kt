package com.example.diamondguesthouse.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.diamondguesthouse.data.dao.RoomsDao
import com.example.diamondguesthouse.data.model.CustomerEntity
import com.example.diamondguesthouse.data.model.RoomEntity
import kotlinx.coroutines.launch

class AddRecordViewModel : ViewModel() {

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

    fun submitRecord(roomDao: RoomsDao) {
        viewModelScope.launch {
            try {
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
                        checkInDate = customerData.checkInDate.value,
                        checkOutDate = customerData.checkOutDate.value,
                        checkInTime = customerData.checkInTime.value,
                        checkOutTime = customerData.checkOutTime.value
                    )
                }

                // Insert both room and customers in one transaction
                roomDao.insertRoomWithCustomers(roomEntity, customerEntities)

            } catch (e: Exception) {
                // Handle the exception (e.g., show a Toast or log the error)
                e.printStackTrace()
            }
        }
    }
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
    var checkInDate: MutableState<Long> = mutableLongStateOf(0L),
    var checkOutDate: MutableState<Long> = mutableLongStateOf(0L),
    var checkInTime: MutableState<Long> = mutableLongStateOf(0L),
    var checkOutTime: MutableState<Long> = mutableLongStateOf(0L),
    var visaUpTill: MutableState<Long?> = mutableStateOf(0L)
)


class AddRecordViewModelFactory(current: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddRecordViewModel::class.java)) {
//            val dao = GuestHouseDatabase.getDatabase(context).customerDao()
            @Suppress("UNCHECKED_CAST")
            return AddRecordViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}