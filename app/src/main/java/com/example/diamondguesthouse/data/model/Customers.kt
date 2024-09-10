package com.example.diamondguesthouse.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String,
    val fatherName: String,
    val cellNo: String,
    val cnic: String?,
    val permanentAddress: String,
    val purposeOfVisit: String,
    val roomNo: String,
    val selectedGender: String,
    val selectedProvince: String?,
    val selectedDistrict: String?,
    val country: String?,
    val lastVisitedCountry: String?,
    val passportNo: String?,
    val noc: String?,
    val checkInDate: Long,
    val checkOutDate: Long,
    val checkInTime: Long,
    val checkOutTime: Long,
    val roomPrice: Double,
    val visaUpTill: Long?
)
