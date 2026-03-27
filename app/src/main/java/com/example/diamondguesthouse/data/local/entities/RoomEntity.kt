package com.example.diamondguesthouse.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey(autoGenerate = true) val roomId: Int? = null,
    val roomNo: String,
    val roomPrice: Double,
    val checkInDate: Long,
    val checkOutDate: Long,
    val checkInTime: Long,
    val checkOutTime: Long,
)
