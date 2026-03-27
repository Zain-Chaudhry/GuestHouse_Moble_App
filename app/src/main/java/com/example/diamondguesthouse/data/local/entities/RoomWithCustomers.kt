package com.example.diamondguesthouse.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class RoomWithCustomers(
    @Embedded val room: RoomEntity,
    @Relation(
        parentColumn = "roomNo",
        entityColumn = "roomNo",
    )
    val customers: List<CustomerEntity>,
)
