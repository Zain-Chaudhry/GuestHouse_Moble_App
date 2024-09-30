package com.example.diamondguesthouse.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.diamondguesthouse.data.model.CustomerEntity
import com.example.diamondguesthouse.data.model.RoomEntity
import com.example.diamondguesthouse.data.model.RoomWithCustomers
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomsDao {

    @Transaction
    @Query("SELECT * FROM rooms")
    fun getAllRoomsWithCustomers(): Flow<List<RoomWithCustomers>>

    @Query("SELECT * FROM rooms")
    fun getAllRooms(): Flow<List<RoomEntity>>

    @Insert
     suspend fun insertRoom(room: RoomEntity)

    @Query("SELECT * FROM rooms WHERE roomNo = :roomNumber")
    suspend fun getRoom(roomNumber: String): RoomEntity?

    @Query("SELECT * FROM rooms WHERE roomNo = :roomNo LIMIT 1")
    suspend fun getRoomByNumber(roomNo: String): RoomEntity?


    @Update
    suspend fun updateRoom(room: RoomEntity)

    @Insert
    suspend fun insertCustomers(customers: List<CustomerEntity>)

    @Transaction
    suspend fun insertRoomWithCustomers(room: RoomEntity, customers: List<CustomerEntity>) {
        insertRoom(room)
        insertCustomers(customers)
    }

    @Query("SELECT COUNT(*) FROM rooms WHERE checkInDate BETWEEN :startDate AND :endDate")
    suspend fun getCountOfRoomsBookedBetween(startDate: Long, endDate: Long): Int

    @Query("SELECT SUM(roomPrice) FROM rooms WHERE checkInDate BETWEEN :startDate AND :endDate")
    suspend fun getTotalIncomeBetweenDates(startDate: Long, endDate: Long): Double?

    @Query("SELECT * FROM rooms WHERE roomNo = :roomNo")
    suspend fun getRoomWithCustomers(roomNo: String): RoomWithCustomers

}
