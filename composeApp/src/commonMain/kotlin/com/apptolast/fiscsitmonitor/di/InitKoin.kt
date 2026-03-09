package com.apptolast.fiscsitmonitor.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform

fun initKoin(config: (KoinApplication.() -> Unit)? = null) {
    if (runCatching { KoinPlatform.getKoin() }.isSuccess) return
    startKoin {
        config?.invoke(this)
        modules(dataModule, presentationModule)
    }
}
