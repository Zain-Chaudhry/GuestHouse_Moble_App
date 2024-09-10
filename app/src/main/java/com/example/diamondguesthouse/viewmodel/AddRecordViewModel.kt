package com.example.diamondguesthouse.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.diamondguesthouse.data.GuestHouseDatabase
import com.example.diamondguesthouse.data.dao.CustomerDao
import com.example.diamondguesthouse.data.model.CustomerEntity
import kotlinx.coroutines.launch

class AddRecordViewModel(val dao: CustomerDao): ViewModel() {

    var name by mutableStateOf("")
    var fatherName by mutableStateOf("")
    var cellNo by mutableStateOf("")
    var cnic by mutableStateOf("")
    var permanentAddress by mutableStateOf("")
    var purposeOfVisit by mutableStateOf("")
    var roomNo by mutableStateOf("")
    var selectedGender by mutableStateOf("Please Select")
    var selectedProvince by mutableStateOf("Please Select")
    var selectedDistrict by mutableStateOf("Please Select")
    var roomPrice by mutableStateOf("")
    var checkInDate by mutableStateOf(System.currentTimeMillis())
    var checkOutDate by mutableStateOf(0L)
    var checkInTime by mutableStateOf(System.currentTimeMillis())
    var checkOutTime by mutableStateOf(0L)

    // Foreign State
    var country by mutableStateOf("")
    var lastVisitedCountry by mutableStateOf("")
    var passportNo by mutableStateOf("")
    var noc by mutableStateOf("")
    var visaUpTill by mutableStateOf(0L)

    fun setGender(gender: String) {
        selectedGender = gender
    }

    fun setProvince(province: String) {
        selectedProvince = province
        selectedDistrict = "Please Select"
    }

    fun setDistrict(district: String) {
        selectedDistrict = district
    }

    fun submitLocalCustomer() {
        val customerEntity = CustomerEntity(
            id = null,
            name = name,
            fatherName = fatherName,
            cellNo = cellNo,
            permanentAddress = permanentAddress,
            purposeOfVisit = purposeOfVisit,
            roomNo = roomNo,
            selectedGender = selectedGender,
            country = null,
            lastVisitedCountry = null,
            passportNo = null,
            noc = null,
            checkInDate = checkInDate,
            checkOutDate = checkOutDate,
            checkInTime = checkInTime,
            checkOutTime = checkOutTime,
            roomPrice = roomPrice.toDoubleOrNull() ?: 0.0,
            selectedProvince = selectedProvince,
            selectedDistrict = selectedDistrict,
            cnic = cnic,
            visaUpTill = null
        )
        viewModelScope.launch {
            try {
                dao.insertCustomer(customerEntity)
            } catch (ex: Throwable) {
                // Handle error
            }
        }
    }

    fun submitForeignCustomer() {
        val customerEntity = CustomerEntity(
            id = null,
            name = name,
            fatherName = fatherName,
            cellNo = cellNo,
            permanentAddress = permanentAddress,
            purposeOfVisit = purposeOfVisit,
            roomNo = roomNo,
            selectedGender = selectedGender,
            country = country,
            lastVisitedCountry = lastVisitedCountry,
            passportNo = passportNo,
            noc = noc,
            checkInDate = checkInDate,
            checkOutDate = checkOutDate,
            checkInTime = checkInTime,
            checkOutTime = checkOutTime,
            roomPrice = roomPrice.toDoubleOrNull() ?: 0.0,
            selectedProvince = null,
            selectedDistrict = null,
            cnic = null,
            visaUpTill = visaUpTill
        )
        viewModelScope.launch {
            try {
               addCustomer(customerEntity)
            } catch (ex: Throwable) {
                // Handle error
            }
        }
    }

    suspend fun addCustomer(customerEntity: CustomerEntity): Boolean {
        return try {
            dao.insertCustomer(customerEntity)
            true
        } catch (ex: Throwable) {
            false
        }

    }
}



class AddRecordViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddRecordViewModel::class.java)) {
            val dao = GuestHouseDatabase.getDatabase(context).customerDao()
            @Suppress("UNCHECKED_CAST")
            return AddRecordViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}