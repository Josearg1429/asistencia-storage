package com.elcarmen.app

import android.os.Build
import java.io.File

/**
 * Utilidades de seguridad del dispositivo.
 * Detecta root y emuladores para bloquear ejecución no autorizada.
 */
object SecurityUtils {

    private val ROOT_PATHS = listOf(
        "/system/app/Superuser.apk",
        "/system/xbin/su",
        "/system/bin/su",
        "/sbin/su",
        "/data/local/su",
        "/data/local/xbin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/data/local/bin/su"
    )

    fun isRooted(): Boolean =
        ROOT_PATHS.any { File(it).exists() } || hasTestKeys()

    private fun hasTestKeys(): Boolean =
        Build.TAGS?.contains("test-keys") == true

    fun isEmulator(): Boolean =
        Build.FINGERPRINT.startsWith("generic") ||
        Build.FINGERPRINT.startsWith("unknown") ||
        Build.MODEL.contains("google_sdk") ||
        Build.MODEL.contains("Emulator") ||
        Build.MODEL.contains("Android SDK built for x86") ||
        Build.MANUFACTURER.contains("Genymotion") ||
        Build.HARDWARE.contains("goldfish") ||
        Build.HARDWARE.contains("ranchu") ||
        Build.PRODUCT.contains("sdk_gphone") ||
        Build.PRODUCT.contains("google_sdk")
}
