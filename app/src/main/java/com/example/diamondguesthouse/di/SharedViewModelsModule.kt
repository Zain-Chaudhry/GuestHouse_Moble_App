package com.example.diamondguesthouse.di

import com.example.diamondguesthouse.presentation.search_booking.SearchBookingScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sharedViewModelsModule = module {
    viewModel { SearchBookingScreenViewModel(get(), get()) }
}
