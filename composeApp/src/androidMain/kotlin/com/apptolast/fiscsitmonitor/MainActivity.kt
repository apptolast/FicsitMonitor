package com.apptolast.fiscsitmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.apptolast.fiscsitmonitor.ads.ConsentManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
