package com.elcarmen.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.webkit.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var secureStorage: SecureStorage

    // Callback pendiente del <input type="file"> del WebView
    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    // Solicitud de permiso de cámara para el escáner QR
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            webView.evaluateJavascript("window._cameraPermissionGranted = true;", null)
        } else {
            webView.evaluateJavascript("window._cameraPermissionGranted = false;", null)
        }
    }

    // Selector de archivos para subir la lista Excel
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        val result = if (uri != null) arrayOf(uri) else null
        filePathCallback?.onReceiveValue(result)
        filePathCallback = null
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ── Seguridad: bloquear capturas de pantalla y grabación ──────────
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        // ── Seguridad: bloquear root y emuladores ─────────────────────────
        if (SecurityUtils.isRooted() || SecurityUtils.isEmulator()) {
            Toast.makeText(this, "Dispositivo no autorizado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        secureStorage = SecureStorage(this)
        webView = findViewById(R.id.webView)

        configurarWebView()
        webView.loadUrl("file:///android_asset/index.html")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configurarWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = false          // Deshabilitado: se usa AndroidStorage (cifrado)
            allowFileAccess = true
            allowContentAccess = true          // Necesario para leer el Uri del archivo seleccionado
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            cacheMode = WebSettings.LOAD_NO_CACHE
            setSupportZoom(false)
            builtInZoomControls = false
            displayZoomControls = false
        }

        // ── Bridge: window.AndroidStorage reemplaza localStorage ──────────
        webView.addJavascriptInterface(StorageBridge(secureStorage), "AndroidStorage")

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                // Notificar al JS que el bridge nativo está listo
                webView.evaluateJavascript("window._androidReady = true;", null)
                // Verificar y solicitar permiso de cámara si hace falta
                verificarPermisoCamara()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                // Bloquear navegación externa — solo file:///android_asset/
                val url = request?.url?.toString() ?: return true
                return !url.startsWith("file:///android_asset/")
            }
        }

        webView.webChromeClient = object : WebChromeClient() {

            // ── Permiso de cámara para el escáner QR ─────────────────────
            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.grant(request.resources)
            }

            // ── Selector de archivos para <input type="file"> ─────────────
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                // Cancelar callback anterior si existe (evita leak)
                this@MainActivity.filePathCallback?.onReceiveValue(null)
                this@MainActivity.filePathCallback = filePathCallback

                // Aceptar .xlsx, .xls y cualquier spreadsheet
                val mimeTypes = arrayOf(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xlsx
                    "application/vnd.ms-excel",                                           // .xls
                    "application/octet-stream",                                           // genérico
                    "*/*"
                )
                filePickerLauncher.launch(mimeTypes)
                return true
            }
        }
    }

    private fun verificarPermisoCamara() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED -> {
                webView.evaluateJavascript("window._cameraPermissionGranted = true;", null)
            }
            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }
}
