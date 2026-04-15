package com.apptolast.fiscsitmonitor.ads

import android.app.Activity
import android.content.pm.ApplicationInfo
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Handles the Google UMP (User Messaging Platform) consent flow on Android and defers
 * MobileAds initialization until `canRequestAds() == true`, in compliance with GDPR/EEA
 * requirements for AdMob publishers (mandatory since Jan 2024).
 *
 * Flow (per https://developers.google.com/admob/android/privacy):
 *   1. (DEBUG only) `consentInformation.reset()` — clears cached consent state so the form
 *      re-appears on every launch. Never call this in production.
 *   2. Build `ConsentRequestParameters` with `DEBUG_GEOGRAPHY_EEA` in debug builds so the
 *      form simulates an EEA device regardless of real IP. Emulators are test devices by
 *      default since UMP 2.2.0, so no `addTestDeviceHashedId` is needed for them. On a
 *      physical device you must copy the hashed ID Logcat prints on first run and add it.
 *   3. `requestConsentInfoUpdate(...)` — async, resolves the user's region + consent status.
 *   4. `loadAndShowConsentFormIfRequired(activity) { ... }` — presents the IAB TCF 2.2 form
 *      if required.
 *   5. Callback resolves → if `canRequestAds()` is true, start Mobile Ads. Otherwise leave
 *      the SDK stopped — banners will render empty.
 *
 * **Precondition outside code**: the publisher (you) must create and **publish** a GDPR
 * message in AdMob Console → Privacy & messaging → GDPR for this app's AdMob App ID. Without
 * a published message UMP has nothing to present and `isConsentFormAvailable` returns false
 * even with the debug geography forced to EEA.
 */
object ConsentManager {
    private const val TAG = "ConsentManager"
    private val mobileAdsInitialized = AtomicBoolean(false)

    fun gatherConsentAndInitAds(activity: Activity) {
        val consentInfo = UserMessagingPlatform.getConsentInformation(activity)
        val isDebug = activity.isDebuggable()

        if (isDebug) {
            // Clear cached consent state so the form reliably re-appears on every launch
            // during development. UMP docs explicitly flag this as "testing purposes only —
            // do not call in production".
            Log.i(TAG, "DEBUG build → resetting cached consent state.")
            consentInfo.reset()
        }

        val params = ConsentRequestParameters.Builder()
            .apply {
                if (isDebug) {
                    setConsentDebugSettings(
                        ConsentDebugSettings.Builder(activity)
                            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                            // Emulators: already test devices by default (UMP 2.2.0+).
                            // Physical devices: copy the hashed ID from the Logcat line
                            //   "Use new ConsentDebugSettings.Builder().addTestDeviceHashedId(\"…\")"
                            // that prints on first run, and add it here:
                            //   .addTestDeviceHashedId("YOUR_DEVICE_HASHED_ID")
                            .build(),
                    )
                }
            }
            .build()

        Log.i(TAG, "requestConsentInfoUpdate(debug=$isDebug) ...")
        consentInfo.requestConsentInfoUpdate(
            activity,
            params,
            {
                Log.i(
                    TAG,
                    "info updated: status=${consentInfo.consentStatus} " +
                            "canRequestAds=${consentInfo.canRequestAds()} " +
                            "formAvailable=${consentInfo.isConsentFormAvailable}",
                )
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    if (formError != null) {
                        Log.w(
                            TAG,
                            "form error: [${formError.errorCode}] ${formError.message}",
                        )
                    } else {
                        Log.i(
                            TAG,
                            "form flow completed. canRequestAds=${consentInfo.canRequestAds()}",
                        )
                    }
                    initializeMobileAdsIfAllowed(activity, consentInfo)
                }
            },
            { requestError ->
                Log.w(
                    TAG,
                    "info update error: [${requestError.errorCode}] ${requestError.message}",
                )
                initializeMobileAdsIfAllowed(activity, consentInfo)
            },
        )
    }

    private fun initializeMobileAdsIfAllowed(activity: Activity, consentInfo: ConsentInformation) {
        if (!consentInfo.canRequestAds()) {
            Log.i(TAG, "canRequestAds=false → skipping MobileAds.initialize().")
            return
        }
        if (!mobileAdsInitialized.compareAndSet(false, true)) return
        Log.i(TAG, "Starting MobileAds SDK.")
        MobileAds.initialize(activity) { /* completed */ }
    }

    private fun Activity.isDebuggable(): Boolean =
        (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
}
