package com.example.diamondguesthouse.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.diamondguesthouse.data.local.dao.CustomerDao
import com.example.diamondguesthouse.data.local.dao.RoomsDao
import com.example.diamondguesthouse.data.local.entities.CustomerEntity
import com.example.diamondguesthouse.data.local.entities.RoomEntity

@Database(
    entities = [RoomEntity::class, CustomerEntity::class],
    version = 5,
    exportSchema = false,
)
abstract class GuestHouseDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomsDao
    abstract fun customerDao(): CustomerDao
}
