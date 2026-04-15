package com.apptolast.fiscsitmonitor.di

import com.apptolast.fiscsitmonitor.presentation.viewmodel.AddServerViewModel
import com.apptolast.fiscsitmonitor.presentation.viewmodel.EnergyViewModel
import com.apptolast.fiscsitmonitor.presentation.viewmodel.FactoryViewModel
import com.apptolast.fiscsitmonitor.presentation.viewmodel.HomeViewModel
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LiveViewModel
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LoginViewModel
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LogisticsViewModel
import com.apptolast.fiscsitmonitor.presentation.viewmodel.RegisterViewModel
import com.apptolast.fiscsitmonitor.presentation.viewmodel.SettingsViewModel
import com.apptolast.fiscsitmonitor.presentation.viewmodel.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::AddServerViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::EnergyViewModel)
    viewModelOf(::FactoryViewModel)
    viewModelOf(::LogisticsViewModel)
    viewModelOf(::LiveViewModel)
}
