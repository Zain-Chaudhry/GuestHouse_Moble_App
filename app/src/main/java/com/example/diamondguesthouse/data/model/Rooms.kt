package com.example.diamondguesthouse.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey val roomNo: String,
    val roomPrice: Double,
    val checkInDate: Long,
    val checkOutDate: Long,
    val checkInTime: Long,
    val checkOutTime: Long,
)

data class RoomWithCustomers(
    @Embedded val room: RoomEntity,
    @Relation(
        parentColumn = "roomNo",
        entityColumn = "roomNo"
    )
    val customers: List<CustomerEntity>
)
