package com.example.diamondguesthouse.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.diamondguesthouse.data.dao.RoomDao
import com.example.diamondguesthouse.data.model.RoomEntity


@Database(entities = [RoomEntity::class], version = 1)
abstract class RoomsDatabase: RoomDatabase() {
    abstract fun roomDao(): RoomDao

    comanion object {
        const val DATABASE_NAME = "rooms_database"
        
    }

}