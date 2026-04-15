package com.apptolast.fiscsitmonitor.ads

import com.apptolast.fiscsitmonitor.BuildKonfig

/**
 * Single source of truth for AdMob Native Advanced Ad unit IDs. Debug builds always resolve to
 * Google test IDs (hardcoded in build.gradle.kts); release builds use the production IDs from
 * local.properties. This prevents accidental hits to the real AdMob account during development.
 */
object AdIds {
    val nativeIdAndroid: String
        get() = if (isDebugBuild) BuildKonfig.ADMOB_NATIVE_ID_ANDROID_TEST
        else BuildKonfig.ADMOB_NATIVE_ID_ANDROID

    val nativeIdIos: String
        get() = if (isDebugBuild) BuildKonfig.ADMOB_NATIVE_ID_IOS_TEST
        else BuildKonfig.ADMOB_NATIVE_ID_IOS
}

expect val isDebugBuild: Boolean
