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
