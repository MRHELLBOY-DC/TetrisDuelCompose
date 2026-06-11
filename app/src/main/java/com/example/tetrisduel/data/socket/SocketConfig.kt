package com.example.tetrisduel.data.socket

import android.os.Build

object SocketConfig {
    private val isEmulator = Build.FINGERPRINT.startsWith("generic")
        || Build.FINGERPRINT.startsWith("unknown")
        || Build.MODEL.contains("google_sdk")
        || Build.MODEL.contains("Emulator")
        || Build.MODEL.contains("Android SDK")
        || Build.MANUFACTURER.contains("Genymotion")
        || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
        || Build.PRODUCT == "google_sdk"

    val SERVER_URL = if (isEmulator) "http://10.0.2.2:3000" else "http://192.168.50.204:3000"
}