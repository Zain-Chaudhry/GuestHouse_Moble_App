package com.example.diamondguesthouse.di

import android.content.Context
import androidx.room.Room
import com.example.diamondguesthouse.data.local.GuestHouseDatabase
import com.example.diamondguesthouse.data.local.dao.CustomerDao
import com.example.diamondguesthouse.data.local.dao.RoomsDao
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseModule {

    @Single
    fun guestHouseDatabase(context: Context): GuestHouseDatabase =
        Room.databaseBuilder(
            context,
            GuestHouseDatabase::class.java,
            "guestHouse_database",
        ).build()

    @Single
    fun roomsDao(db: GuestHouseDatabase): RoomsDao = db.roomDao()

    @Single
    fun customerDao(db: GuestHouseDatabase): CustomerDao = db.customerDao()
}
