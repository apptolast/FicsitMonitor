package com.apptolast.fiscsitmonitor.di

import com.apptolast.fiscsitmonitor.data.remote.api.SatisfactoryApiService
import com.apptolast.fiscsitmonitor.data.remote.createHttpClient
import com.apptolast.fiscsitmonitor.data.repository.EnergyRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.FactoryRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.LogisticsRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.ServerRepositoryImpl
import com.apptolast.fiscsitmonitor.domain.repository.EnergyRepository
import com.apptolast.fiscsitmonitor.domain.repository.FactoryRepository
import com.apptolast.fiscsitmonitor.domain.repository.LogisticsRepository
import com.apptolast.fiscsitmonitor.domain.repository.ServerRepository
import org.koin.dsl.module

val dataModule = module {
    single { createHttpClient() }
    single { SatisfactoryApiService(get()) }
    single { ServerRepositoryImpl(get()) }
    single<ServerRepository> { get<ServerRepositoryImpl>() }
    single { EnergyRepositoryImpl(get()) }
    single<EnergyRepository> { get<EnergyRepositoryImpl>() }
    single { FactoryRepositoryImpl() }
    single<FactoryRepository> { get<FactoryRepositoryImpl>() }
    single { LogisticsRepositoryImpl(get(), get()) }
    single<LogisticsRepository> { get<LogisticsRepositoryImpl>() }
}
