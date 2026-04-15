package com.apptolast.fiscsitmonitor.ads

import com.apptolast.fiscsitmonitor.BuildKonfig

/**
 * Single source of truth for AdMob ad unit IDs. Debug builds always resolve to the Google test
 * IDs (hardcoded in `build.gradle.kts`), release builds to the production IDs read from
 * `local.properties`. Keeps developer machines from ever hitting the real AdMob account — a
 * single click on your own production banner while testing is enough for Google to suspend it.
 */
object AdIds {
    val bannerIdAndroid: String
        get() = if (isDebugBuild) BuildKonfig.ADMOB_BANNER_ID_ANDROID_TEST
        else BuildKonfig.ADMOB_BANNER_ID_ANDROID

    val bannerIdIos: String
        get() = if (isDebugBuild) BuildKonfig.ADMOB_BANNER_ID_IOS_TEST
        else BuildKonfig.ADMOB_BANNER_ID_IOS
}

expect val isDebugBuild: Boolean
