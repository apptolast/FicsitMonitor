import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.buildkonfig)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) load(file.inputStream())
}

// Google test IDs (see https://developers.google.com/admob/android/test-ads and /admob/ios/test-ads).
// These are ALWAYS used in debug builds and as a safe fallback when local.properties is missing
// the real IDs — never ship these to production.
private val testAdmobAppIdAndroid = "ca-app-pub-3940256099942544~3347511713"
private val testAdmobAppIdIos = "ca-app-pub-3940256099942544~1458002511"
// Native Advanced Ad test IDs (https://developers.google.com/admob/android/native/start)
private val testAdmobNativeIdAndroid = "ca-app-pub-3940256099942544/2247696110"
private val testAdmobNativeIdIos = "ca-app-pub-3940256099942544/3986624511"
// Standard Banner test IDs (https://developers.google.com/admob/android/banner)
private val testAdmobBannerIdAndroid = "ca-app-pub-3940256099942544/6300978111"
private val testAdmobBannerIdIos = "ca-app-pub-3940256099942544/2934735716"

// Production IDs come from local.properties; if absent we fall back to the Google test IDs so
// the project still builds on a clean checkout. A release build without real IDs in
// local.properties will simply ship test ads — intentional, not a silent miscompilation.
val admobAppIdAndroidProd: String = localProperties.getProperty("ADMOB_APP_ID_ANDROID", testAdmobAppIdAndroid)
val admobAppIdIosProd: String = localProperties.getProperty("ADMOB_APP_ID_IOS", testAdmobAppIdIos)
val admobNativeIdAndroidProd: String = localProperties.getProperty("ADMOB_NATIVE_ID_ANDROID", testAdmobNativeIdAndroid)
val admobNativeIdIosProd: String = localProperties.getProperty("ADMOB_NATIVE_ID_IOS", testAdmobNativeIdIos)
val admobBannerIdAndroidProd: String = localProperties.getProperty("ADMOB_BANNER_ID_ANDROID", testAdmobBannerIdAndroid)
val admobBannerIdIosProd: String = localProperties.getProperty("ADMOB_BANNER_ID_IOS", testAdmobBannerIdIos)

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.appcompat)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
            implementation(libs.play.services.ads)
            implementation(libs.google.ump)
        }
        commonMain.dependencies {
            // Compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.uiToolingPreview)

            // Lifecycle & Navigation
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.navigation.compose)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.websockets)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Kotlinx
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)

            // Multiplatform Settings
            implementation(libs.multiplatform.settings.no.arg)
            implementation(libs.multiplatform.settings.coroutines)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.apptolast.fiscsitmonitor"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    buildFeatures {
        // Needed for `BuildConfig.DEBUG` — used by `AdIds` to pick test vs prod ad unit IDs at
        // runtime without a separate expect/actual helper that carries a Context.
        buildConfig = true
    }
    defaultConfig {
        applicationId = "com.apptolast.fiscsitmonitor"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 3
        versionName = "1.0.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    val storeFilePath = localProperties.getProperty("signing.storeFile")
    if (storeFilePath != null) {
        signingConfigs {
            create("release") {
                storeFile = file(storeFilePath)
                storePassword = localProperties.getProperty("signing.storePassword")
                keyAlias = localProperties.getProperty("signing.keyAlias")
                keyPassword = localProperties.getProperty("signing.keyPassword")
            }
        }
    }
    buildTypes {
        getByName("debug") {
            // Force the AndroidManifest `<meta-data>` to the Google test App ID so debug builds
            // can never accidentally initialise MobileAds against the production AdMob account.
            manifestPlaceholders["admobAppId"] = testAdmobAppIdAndroid
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfigs.findByName("release")?.let { signingConfig = it }
            manifestPlaceholders["admobAppId"] = admobAppIdAndroidProd
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

buildkonfig {
    packageName = "com.apptolast.fiscsitmonitor"

    defaultConfigs {
        buildConfigField(
            STRING,
            "API_BASE_URL",
            localProperties.getProperty("API_BASE_URL", "https://satisfactory-dashboard.pablohgdev.com")
        )
        // Native Advanced Ad IDs — production from local.properties, test always hardcoded.
        // `commonMain/ads/AdIds.kt` picks between prod/test based on `isDebugBuild`.
        buildConfigField(STRING, "ADMOB_NATIVE_ID_ANDROID", admobNativeIdAndroidProd)
        buildConfigField(STRING, "ADMOB_NATIVE_ID_IOS", admobNativeIdIosProd)
        buildConfigField(STRING, "ADMOB_NATIVE_ID_ANDROID_TEST", testAdmobNativeIdAndroid)
        buildConfigField(STRING, "ADMOB_NATIVE_ID_IOS_TEST", testAdmobNativeIdIos)
        // Standard Banner Ad IDs — production from local.properties, test always hardcoded.
        buildConfigField(STRING, "ADMOB_BANNER_ID_ANDROID", admobBannerIdAndroidProd)
        buildConfigField(STRING, "ADMOB_BANNER_ID_IOS", admobBannerIdIosProd)
        buildConfigField(STRING, "ADMOB_BANNER_ID_ANDROID_TEST", testAdmobBannerIdAndroid)
        buildConfigField(STRING, "ADMOB_BANNER_ID_IOS_TEST", testAdmobBannerIdIos)
    }
}
