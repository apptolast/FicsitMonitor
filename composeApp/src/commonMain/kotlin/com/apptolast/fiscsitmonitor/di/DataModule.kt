package com.apptolast.fiscsitmonitor.di

import com.apptolast.fiscsitmonitor.data.auth.AuthApiService
import com.apptolast.fiscsitmonitor.data.bootstrap.ServerBootstrapper
import com.apptolast.fiscsitmonitor.data.remote.Environment
import com.apptolast.fiscsitmonitor.data.remote.api.ConfigApiService
import com.apptolast.fiscsitmonitor.data.remote.api.DashboardApiService
import com.apptolast.fiscsitmonitor.data.remote.createHttpClient
import com.apptolast.fiscsitmonitor.data.remote.websocket.ReverbAuthorizer
import com.apptolast.fiscsitmonitor.data.remote.websocket.ReverbWebSocketClient
import com.apptolast.fiscsitmonitor.data.remote.websocket.WebSocketEventDispatcher
import com.apptolast.fiscsitmonitor.data.repository.AuthRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.EnergyRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.FactoryRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.LogisticsRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.ServerRepositoryImpl
import com.apptolast.fiscsitmonitor.data.repository.UserServerRepositoryImpl
import com.apptolast.fiscsitmonitor.data.server.ServerApiService
import com.apptolast.fiscsitmonitor.data.session.AuthSession
import com.apptolast.fiscsitmonitor.data.session.ServerShadowStore
import com.apptolast.fiscsitmonitor.data.session.SessionStorage
import com.apptolast.fiscsitmonitor.domain.repository.AuthRepository
import com.apptolast.fiscsitmonitor.domain.repository.EnergyRepository
import com.apptolast.fiscsitmonitor.domain.repository.FactoryRepository
import com.apptolast.fiscsitmonitor.domain.repository.LogisticsRepository
import com.apptolast.fiscsitmonitor.domain.repository.ServerRepository
import com.apptolast.fiscsitmonitor.domain.repository.UserServerRepository
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val dataModule = module {
    // Session + storage
    single<Settings> { Settings() }
    single { SessionStorage(get()) }
    single { AuthSession(get()) }
    single { ServerShadowStore(get()) }

    // Env + HTTP
    single { Environment(get()) }
    single { createHttpClient(get(), get()) }

    // API services
    single { AuthApiService(get(), get()) }
    single { ServerApiService(get(), get()) }
    single { ConfigApiService(get(), get()) }
    single { DashboardApiService(get(), get()) }

    // WebSocket
    single { ReverbAuthorizer(get(), get()) }
    single { ReverbWebSocketClient(get(), get(), get()) }

    // Repositories
    single { AuthRepositoryImpl(get(), get()) }
    single<AuthRepository> { get<AuthRepositoryImpl>() }

    single { UserServerRepositoryImpl(get(), get()) }
    single<UserServerRepository> { get<UserServerRepositoryImpl>() }

    single { ServerRepositoryImpl() }
    single<ServerRepository> { get<ServerRepositoryImpl>() }

    single { EnergyRepositoryImpl(get()) }
    single<EnergyRepository> { get<EnergyRepositoryImpl>() }

    single { FactoryRepositoryImpl() }
    single<FactoryRepository> { get<FactoryRepositoryImpl>() }

    single { LogisticsRepositoryImpl(get()) }
    single<LogisticsRepository> { get<LogisticsRepositoryImpl>() }

    // WS event dispatcher + bootstrapper
    single {
        WebSocketEventDispatcher(
            webSocketClient = get(),
            serverRepository = get(),
            energyRepository = get(),
            factoryRepository = get(),
            logisticsRepository = get(),
        )
    }
    single {
        ServerBootstrapper(
            session = get(),
            dashboardApi = get(),
            webSocketClient = get(),
            webSocketEventDispatcher = get(),
            serverRepository = get(),
            energyRepository = get(),
            factoryRepository = get(),
            logisticsRepository = get(),
        )
    }
}
