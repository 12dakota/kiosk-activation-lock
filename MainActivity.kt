package com.yourcompany.kiosklock

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create notification channel for Android O and above
        NotificationChannelSetup.createNotificationChannel(this)

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }

        // Start the license activation service if device is not activated
        if (!LicenseManager.isDeviceActivated(this)) {
            val serviceIntent = Intent(this, LicenseActivationService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        }
    }

    // Example: Call this when payment is received
    fun onPaymentReceived() {
        LicenseManager.activateDevice(this)
        // Show success message or update UI
    }

    // Example: Call this to test deactivation
    fun onTestDeactivation() {
        LicenseManager.deactivateDevice(this)
    }
}