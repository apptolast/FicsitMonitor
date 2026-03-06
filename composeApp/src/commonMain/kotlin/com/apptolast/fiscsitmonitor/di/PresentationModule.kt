package com.apptolast.fiscsitmonitor.di

import com.apptolast.fiscsitmonitor.presentation.viewmodel.EnergyViewModel
import com.apptolast.fiscsitmonitor.presentation.viewmodel.FactoryViewModel
import com.apptolast.fiscsitmonitor.presentation.viewmodel.HomeViewModel
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LiveViewModel
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LogisticsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::EnergyViewModel)
    viewModelOf(::FactoryViewModel)
    viewModelOf(::LogisticsViewModel)
    viewModelOf(::LiveViewModel)
}
