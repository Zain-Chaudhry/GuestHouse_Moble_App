package com.example.diamondguesthouse

import android.app.Application
import com.example.diamondguesthouse.di.AppKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.plugin.module.dsl.startKoin

class GuestHouseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin<AppKoin> {
            androidLogger()
            androidContext(this@GuestHouseApplication)
        }
    }
}
