package com.example.diamondguesthouse.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diamondguesthouse.data.dao.CustomerDao
import com.example.diamondguesthouse.data.dao.RoomsDao
import com.example.diamondguesthouse.data.model.CustomerEntity
import com.example.diamondguesthouse.data.model.RoomEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchRecordViewModel : ViewModel() {

    var roomNo: String by mutableStateOf("Please Select")
    var roomPrice: String by mutableStateOf("")
    private var checkInDate: Long by mutableLongStateOf(System.currentTimeMillis())
    var checkOutDate: Long by mutableLongStateOf(0L)
    private var checkInTime: Long by mutableLongStateOf(System.currentTimeMillis())
    var checkOutTime: Long by mutableLongStateOf(0L)

    var cnic by mutableStateOf("")
    var passportNo by mutableStateOf("")
    var searchResults = mutableStateListOf<CustomerEntity>()// Update to a list
    var errorMessage by mutableStateOf("")

    //    var selectedCustomers = mutableStateListOf<CustomerEntity>()
    var selectedCustomers by mutableStateOf(listOf<CustomerEntity>())


    fun addCustomer(customer: CustomerEntity) {
        if (!selectedCustomers.contains(customer)) {
            val updatedList = selectedCustomers.toMutableList()
            updatedList.add(customer)
            selectedCustomers = updatedList

        }
    }

    fun removeCustomer(customer: CustomerEntity) {
        val updatedList = selectedCustomers.toMutableList()
        updatedList.remove(customer)
        selectedCustomers = updatedList

    }

    fun searchCustomer(customerDao: CustomerDao) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val results = customerDao.getCustomerByCnicOrPassport(cnic.ifBlank { null },
                    passportNo.ifBlank { null })
                withContext(Dispatchers.Main) {
                    if (results.isNotEmpty()) {

                        // Add new results to existing searchResults
                        searchResults.addAll(results.filter { it !in searchResults })
                        errorMessage = "" // Clear error message on successful search
                    } else {
                        errorMessage = "No customers found"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Error occurred: ${e.message}"
                }
            }
        }
    }

    fun confirmBooking(roomDao: RoomsDao) {
        viewModelScope.launch {
            try {
                // Check if the room number already exists
                val existingRoom = roomDao.getRoomByNumber(roomNo)
                if (existingRoom != null) {
                    Log.e("SearchRecordViewModel", "Room with number $roomNo already exists.")
                    // Optionally, handle the situation (e.g., show a message to the user)
                    return@launch
                }

                // Create a RoomEntity instance with the provided room details
                val roomEntity = RoomEntity(
                    roomNo = roomNo,
                    roomPrice = roomPrice.toDouble(),
                    checkInDate = checkInDate,
                    checkOutDate = checkOutDate,
                    checkInTime = checkInTime,
                    checkOutTime = checkOutTime
                )

                // Prepare customer entities for insertion
                val customerEntities = selectedCustomers.map { customerData ->
                    CustomerEntity(
                        customerId = null, // Use null for auto-increment
                        roomNo = roomEntity.roomNo, // Associate with the room
                        name = customerData.name,
                        fatherName = customerData.fatherName,
                        cellNo = customerData.cellNo,
                        cnic = customerData.cnic,
                        permanentAddress = customerData.permanentAddress,
                        selectedGender = customerData.selectedGender,
                        country = customerData.country,
                        passportNo = customerData.passportNo,
                        visaUpTill = customerData.visaUpTill,
                        checkInDate = customerData.checkInDate,
                        checkOutDate = customerData.checkOutDate,
                        checkInTime = customerData.checkInTime,
                        checkOutTime = customerData.checkOutTime
                    )
                }

                // Insert both room and customers in one transaction
                roomDao.insertRoomWithCustomers(roomEntity, customerEntities)

            } catch (e: Exception) {
                // Handle the exception (e.g., show a Toast or log the error)
                e.printStackTrace()
                Log.e("SearchRecordViewModel", "Error occurred: ${e.message}")
            }
        }
    }


//    // Reset the search fields and results
//    fun resetSearch() {
//        cnic = ""
//        passportNo = ""
//        searchResults.clear()
//        errorMessage = ""
//    }
}


