package com.yourcompany.kiosklock

import android.content.Context
import android.content.Intent

object LicenseManager {

    private const val PREFS_NAME = "license_prefs"
    private const val KEY_ACTIVATED = "is_activated"
    private const val KEY_ACTIVATION_DATE = "activation_date"

    /**
     * Mark device as activated and remove the notification
     */
    fun activateDevice(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().apply {
            putBoolean(KEY_ACTIVATED, true)
            putLong(KEY_ACTIVATION_DATE, System.currentTimeMillis())
            apply()
        }

        // Stop the service and remove notification
        val serviceIntent = Intent(context, LicenseActivationService::class.java)
        context.stopService(serviceIntent)
    }

    /**
     * Deactivate device and show notification again
     */
    fun deactivateDevice(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().apply {
            putBoolean(KEY_ACTIVATED, false)
            remove(KEY_ACTIVATION_DATE)
            apply()
        }

        // Start the service to show notification
        val serviceIntent = Intent(context, LicenseActivationService::class.java)
        context.startService(serviceIntent)
    }

    /**
     * Check if device is currently activated
     */
    fun isDeviceActivated(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(KEY_ACTIVATED, false)
    }

    /**
     * Get activation date
     */
    fun getActivationDate(context: Context): Long {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getLong(KEY_ACTIVATION_DATE, 0L)
    }
}