package com.apptolast.fiscsitmonitor

import android.app.Application
import com.apptolast.fiscsitmonitor.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class FicsitMonitorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@FicsitMonitorApplication)
        }
    }
}
