package com.example.diamondguesthouse.domain.models

data class RoomModel(
    val roomId: Int? = null,
    val roomNo: String,
    val roomPrice: Double,
    val checkInDate: Long,
    val checkOutDate: Long,
    val checkInTime: Long,
    val checkOutTime: Long,
)
