package com.elcarmen.app

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Reemplaza localStorage del navegador con almacenamiento cifrado AES-256-GCM.
 * Los datos se almacenan en EncryptedSharedPreferences — no legibles
 * aunque el dispositivo sea extraído o el APK sea desensamblado.
 */
class SecureStorage(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "elcarmen_secure_v5",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun setItem(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getItem(key: String): String? = prefs.getString(key, null)

    fun removeItem(key: String) {
        prefs.edit().remove(key).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun length(): Int = prefs.all.size

    fun key(index: Int): String? = prefs.all.keys.toList().getOrNull(index)
}
