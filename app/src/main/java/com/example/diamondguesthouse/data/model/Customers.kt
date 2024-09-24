package com.example.diamondguesthouse.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val customerId: Int? = null,
    val roomNo: String,
    val name: String,
    val fatherName: String,
    val cellNo: String,
    val cnic: String?,
    val permanentAddress: String,
    val selectedGender: String,
    val country: String?,
    val passportNo: String?,
    val checkInDate: Long,
    val checkOutDate: Long,
    val checkInTime: Long,
    val checkOutTime: Long,
    val visaUpTill: Long?
)
