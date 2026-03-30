package com.example.diamondguesthouse.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DatabaseModule::class])
@ComponentScan("com.example.diamondguesthouse")
class AppModule
