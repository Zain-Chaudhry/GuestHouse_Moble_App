package com.example.diamondguesthouse.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.diamondguesthouse.data.dao.CustomerDao
import com.example.diamondguesthouse.data.model.CustomerEntity


@Database(entities = [CustomerEntity::class], version = 2)
abstract class GuestHouseDatabase: RoomDatabase() {
    abstract fun customerDao(): CustomerDao

    companion object {
        const val DATABASE_NAME = "guestHouse_database"
        fun getDatabase(context: Context): GuestHouseDatabase {
            return Room.databaseBuilder(
                context,
                GuestHouseDatabase::class.java,
                DATABASE_NAME
            ).build()
        }

    }

}