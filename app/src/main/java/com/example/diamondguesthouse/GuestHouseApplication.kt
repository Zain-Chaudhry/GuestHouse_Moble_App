package com.example.diamondguesthouse

import android.app.Application
import com.example.diamondguesthouse.di.AppModule
import com.example.diamondguesthouse.di.DatabaseModule
import com.example.diamondguesthouse.di.presentationModule
import com.example.diamondguesthouse.di.sharedViewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class GuestHouseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@GuestHouseApplication)
            modules(
                DatabaseModule().module,
                AppModule().module,
                presentationModule,
                sharedViewModelsModule,
            )
        }
    }
}
