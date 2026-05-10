package com.elcarmen.app

import android.webkit.JavascriptInterface

/**
 * Puente JS ↔ Kotlin expuesto como window.AndroidStorage en el WebView.
 * El JavaScript llama a estos métodos exactamente igual que localStorage,
 * pero los datos se cifran con AES-256-GCM antes de persistir.
 *
 * Nota: getItem devuelve la cadena "null" (no null JS) cuando la clave
 * no existe — el wrapper Store en script.js convierte "null" → null.
 */
class StorageBridge(private val storage: SecureStorage) {

    @JavascriptInterface
    fun setItem(key: String, value: String) = storage.setItem(key, value)

    @JavascriptInterface
    fun getItem(key: String): String = storage.getItem(key) ?: "null"

    @JavascriptInterface
    fun removeItem(key: String) = storage.removeItem(key)

    @JavascriptInterface
    fun clear() = storage.clear()

    @JavascriptInterface
    fun length(): Int = storage.length()

    @JavascriptInterface
    fun key(index: Int): String = storage.key(index) ?: "null"
}
