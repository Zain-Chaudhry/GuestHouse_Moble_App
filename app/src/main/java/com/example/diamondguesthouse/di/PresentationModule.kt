package com.example.diamondguesthouse.di

import com.example.diamondguesthouse.core.EntityModelMapper
import com.example.diamondguesthouse.core.utils.InitialSplash
import com.example.diamondguesthouse.data.local.entities.CustomerEntity
import com.example.diamondguesthouse.data.local.entities.RoomEntity
import com.example.diamondguesthouse.data.local.entities.RoomWithCustomers
import com.example.diamondguesthouse.data.mapper.CustomerEntityModelMapper
import com.example.diamondguesthouse.data.mapper.RoomEntityModelMapper
import com.example.diamondguesthouse.data.mapper.RoomWithCustomersModelMapper
import com.example.diamondguesthouse.domain.models.CustomerModel
import com.example.diamondguesthouse.domain.models.RoomModel
import com.example.diamondguesthouse.domain.models.RoomWithCustomersModel
import com.example.diamondguesthouse.presentation.add_record_screen.AddRecordScreenViewModel
import com.example.diamondguesthouse.presentation.forgot_password_screen.ForgotPasswordScreenViewModel
import com.example.diamondguesthouse.presentation.home_screen.HomeScreenViewModel
import com.example.diamondguesthouse.presentation.login_screen.LoginScreenViewModel
import com.example.diamondguesthouse.presentation.report_screen.ReportScreenViewModel
import com.example.diamondguesthouse.presentation.setting_screen.SettingScreenViewModel
import com.example.diamondguesthouse.presentation.sign_up_screen.SignUpScreenViewModel
import com.example.diamondguesthouse.presentation.view_booking_screen.ViewBookingScreenViewModel
import com.example.diamondguesthouse.presentation.view_check_outs_screen.ViewCheckOutsScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presentationModule = module {
    single { InitialSplash() }
    factory<EntityModelMapper<RoomEntity, RoomModel>>(named(MapperQualifiers.ROOM)) {
        RoomEntityModelMapper()
    }
    factory<EntityModelMapper<CustomerEntity, CustomerModel>>(named(MapperQualifiers.CUSTOMER)) {
        CustomerEntityModelMapper()
    }
    factory<EntityModelMapper<RoomWithCustomers, RoomWithCustomersModel>>(
        named(MapperQualifiers.ROOM_WITH_CUSTOMERS),
    ) {
        RoomWithCustomersModelMapper(
            get(named(MapperQualifiers.ROOM)),
            get(named(MapperQualifiers.CUSTOMER)),
        )
    }
    viewModel { LoginScreenViewModel(get()) }
    viewModel { SignUpScreenViewModel(get()) }
    viewModel { ForgotPasswordScreenViewModel(get()) }
    viewModel { HomeScreenViewModel(get()) }
    viewModel { AddRecordScreenViewModel(get(), get()) }
    viewModel { ReportScreenViewModel(get()) }
    viewModel { SettingScreenViewModel(get()) }
    viewModel { ViewBookingScreenViewModel(get()) }
    viewModel { ViewCheckOutsScreenViewModel(get()) }
}
