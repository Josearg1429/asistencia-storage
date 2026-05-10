# Mantener el bridge JS↔Kotlin expuesto via @JavascriptInterface
-keepclassmembers class com.elcarmen.app.StorageBridge {
    @android.webkit.JavascriptInterface <methods>;
}

# Mantener clases de seguridad
-keep class com.elcarmen.app.SecureStorage { *; }
-keep class com.elcarmen.app.SecurityUtils { *; }

# Ofuscación agresiva
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-verbose

# Eliminar logs en producción
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Ocultar nombre del archivo fuente en stack traces
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# AndroidX Security Crypto
-keep class androidx.security.crypto.** { *; }
-keep class com.google.crypto.tink.** { *; }

# Dependencias opcionales de Tink no incluidas en el APK (solo se usan en entornos server-side)
# R8 las reporta como "missing" pero no se invocan en runtime en Android
-dontwarn com.google.api.client.http.**
-dontwarn javax.annotation.**
-dontwarn javax.annotation.concurrent.**
-dontwarn org.joda.time.**
-dontwarn com.google.errorprone.annotations.**
-dontwarn com.google.j2objc.annotations.**
