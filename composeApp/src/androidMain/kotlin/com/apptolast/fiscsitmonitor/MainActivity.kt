package com.apptolast.fiscsitmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.apptolast.fiscsitmonitor.ads.ConsentManager
import com.apptolast.fiscsitmonitor.data.session.SessionStorage
import com.apptolast.fiscsitmonitor.platform.applyAppLocale
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val storage: SessionStorage by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        applyAppLocale(storage.userLocale)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Kick off the GDPR/UMP consent flow. This runs asynchronously and will show the
        // consent form if the user hasn't answered yet (or if the configuration changed).
        // When the flow resolves, MobileAds.initialize() is called conditionally on
        // `canRequestAds`, so no ads network calls happen before user consent.
        ConsentManager.gatherConsentAndInitAds(this)

        setContent {
            App()
        }
    }
}
