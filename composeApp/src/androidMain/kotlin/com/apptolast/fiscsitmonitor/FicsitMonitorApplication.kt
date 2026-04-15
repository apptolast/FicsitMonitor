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
        // Note: MobileAds.initialize() is intentionally NOT called here. It is gated on the
        // UMP (GDPR) consent flow and triggered from MainActivity.onCreate via ConsentManager,
        // so we don't make ad server calls before the user has consented.
    }
}
