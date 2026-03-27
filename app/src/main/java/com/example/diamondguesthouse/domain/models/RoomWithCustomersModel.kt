package com.example.diamondguesthouse.domain.models

data class RoomWithCustomersModel(
    val room: RoomModel,
    val customers: List<CustomerModel>,
)
