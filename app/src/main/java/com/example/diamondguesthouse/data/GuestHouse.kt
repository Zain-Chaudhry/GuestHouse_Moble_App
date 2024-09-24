package com.example.diamondguesthouse.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.diamondguesthouse.data.dao.CustomerDao
import com.example.diamondguesthouse.data.dao.RoomsDao
import com.example.diamondguesthouse.data.model.CustomerEntity
import com.example.diamondguesthouse.data.model.RoomEntity

@Database(entities = [RoomEntity::class, CustomerEntity::class], version = 4, exportSchema = false)
abstract class GuestHouseDatabase: RoomDatabase() {
    abstract fun roomDao(): RoomsDao
    abstract fun customerDao(): CustomerDao

    companion object {
        const val DATABASE_NAME = "guestHouse_database"

        @Volatile
        private var INSTANCE: GuestHouseDatabase? = null

        fun getDatabase(context: Context): GuestHouseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GuestHouseDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()  // This will allow destructive migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
