package com.yourcompany.kiosklock

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var activateButton: Button
    private lateinit var deactivateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        statusText = findViewById(R.id.statusText)
        activateButton = findViewById(R.id.activateButton)
        deactivateButton = findViewById(R.id.deactivateButton)

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

        // Setup button listeners
        activateButton.setOnClickListener {
            onPaymentReceived()
        }

        deactivateButton.setOnClickListener {
            onTestDeactivation()
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

        // Update UI with current status
        updateStatusUI()
    }

    override fun onResume() {
        super.onResume()
        updateStatusUI()
    }

    private fun updateStatusUI() {
        val isActivated = LicenseManager.isDeviceActivated(this)
        if (isActivated) {
            statusText.text = getString(R.string.status_active)
            statusText.setTextColor(getColor(android.R.color.holo_green_dark))
        } else {
            statusText.text = getString(R.string.status_inactive)
            statusText.setTextColor(getColor(android.R.color.holo_red_dark))
        }
    }

    /**
     * Call this when payment is received
     */
    private fun onPaymentReceived() {
        LicenseManager.activateDevice(this)
        Toast.makeText(this, R.string.device_activated, Toast.LENGTH_SHORT).show()
        updateStatusUI()
    }

    /**
     * Call this to test deactivation
     */
    private fun onTestDeactivation() {
        LicenseManager.deactivateDevice(this)
        Toast.makeText(this, R.string.device_deactivated, Toast.LENGTH_SHORT).show()
        updateStatusUI()
    }
}