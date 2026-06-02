package com.yourcompany.kiosklock

import android.app.Service
import android.app.NotificationManager
import android.content.Intent
import android.content.Context
import android.os.IBinder
import androidx.core.app.NotificationCompat

class LicenseActivationService : Service() {

    private lateinit var notificationManager: NotificationManager
    private val NOTIFICATION_ID = 1001
    private val CHANNEL_ID = "license_activation_channel"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if device is activated
        val isActivated = checkDeviceActivation()

        if (!isActivated) {
            showPersistentNotification()
        }

        return START_STICKY
    }

    private fun showPersistentNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Device Not Active")
            .setContentText("Your device is not active. Please purchase an activation license.")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setOngoing(true) // Makes notification persistent
            .setAutoCancel(false) // Prevents dismissal
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SYSTEM)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun checkDeviceActivation(): Boolean {
        // Get SharedPreferences to check activation status
        val sharedPref = getSharedPreferences("license_prefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("is_activated", false)
    }

    fun clearNotification() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}