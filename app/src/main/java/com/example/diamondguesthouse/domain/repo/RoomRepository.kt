package com.example.diamondguesthouse.domain.repo

import com.example.diamondguesthouse.domain.models.CustomerModel
import com.example.diamondguesthouse.domain.models.RoomModel
import com.example.diamondguesthouse.domain.models.RoomWithCustomersModel
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun getAllRoomsWithCustomers(): Flow<List<RoomWithCustomersModel>>
    suspend fun insertRoomWithCustomers(room: RoomModel, customers: List<CustomerModel>)
    suspend fun getRoomByNumber(roomNo: String): RoomModel?
    suspend fun getBookedCountBetween(startDate: Long, endDate: Long): Int
    suspend fun getTotalIncomeBetween(startDate: Long, endDate: Long): Double
}
