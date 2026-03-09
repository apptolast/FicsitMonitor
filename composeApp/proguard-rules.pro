# ============================================================================
# FicsitMonitor - ProGuard / R8 rules
# ============================================================================

# ---------- Kotlinx Serialization ----------
# Keep @Serializable classes and their generated serializers
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# App DTOs
-keep,includedescriptorclasses class com.apptolast.fiscsitmonitor.data.model.**$$serializer { *; }
-keepclassmembers class com.apptolast.fiscsitmonitor.data.model.** {
    *** Companion;
}
-keepclasseswithmembers class com.apptolast.fiscsitmonitor.data.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ---------- Ktor ----------
-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { volatile <fields>; }
-keep class io.ktor.client.engine.okhttp.** { *; }
-dontwarn io.ktor.**

# ---------- OkHttp / Okio ----------
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-keep class okhttp3.internal.publicsuffix.PublicSuffixDatabase { *; }

# ---------- Koin ----------
-keep class org.koin.** { *; }
-keepclassmembers class * {
    @org.koin.core.annotation.* <methods>;
}

# ---------- Kotlin Coroutines ----------
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# ---------- Compose ----------
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# ---------- BuildKonfig ----------
-keep class com.apptolast.fiscsitmonitor.BuildKonfig { *; }

# ---------- General ----------
# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
