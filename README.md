# Kiosk Activation Lock

Android Kotlin native implementation of a persistent device activation notification system for kiosk POS devices.

## Quick Start

1. **Add the files** to your Android project under `src/main/kotlin/com/yourcompany/kiosklock/`
2. **Update AndroidManifest.xml** with the required permissions and services
3. **Initialize in MainActivity**:
   ```kotlin
   NotificationChannelSetup.createNotificationChannel(this)
   if (!LicenseManager.isDeviceActivated(this)) {
       startForegroundService(Intent(this, LicenseActivationService::class.java))
   }
   ```
4. **Activate after payment**:
   ```kotlin
   LicenseManager.activateDevice(context)
   ```

## Features

- ✅ Persistent notification that cannot be dismissed
- ✅ Automatically starts on device boot
- ✅ Survives device restarts
- ✅ Works in kiosk mode
- ✅ Simple API for activation/deactivation
- ✅ Android O+ compatible

## How It Works

The system uses a foreground service to display a persistent notification with the message:

> "Your device is not active. Please purchase an activation license."

The notification:
- Appears in the notification tray
- Cannot be swiped away
- Persists until the device is activated
- Automatically reappears if deactivated
- Shows on every device boot

## API

### Check Activation Status
```kotlin
val isActivated = LicenseManager.isDeviceActivated(context)
```

### Activate Device
```kotlin
LicenseManager.activateDevice(context)
```

### Deactivate Device
```kotlin
LicenseManager.deactivateDevice(context)
```

### Get Activation Date
```kotlin
val activationDate = LicenseManager.getActivationDate(context)
```

## Files

- **LicenseActivationService.kt** - Foreground service managing the notification
- **BootReceiver.kt** - Boot completion receiver to start service on device startup
- **LicenseManager.kt** - Utility for managing activation state
- **NotificationChannelSetup.kt** - Notification channel creation for Android O+
- **MainActivity.kt** - Example activity showing initialization
- **AndroidManifest.xml** - Required manifest configuration
- **SETUP_GUIDE.md** - Detailed setup and customization guide

## Integration with POS System

When payment is received in your POS app:

```kotlin
fun onPaymentReceived(paymentId: String) {
    // Verify payment with backend
    if (verifyPayment(paymentId)) {
        // Activate the device
        LicenseManager.activateDevice(this)
    }
}
```

When you need to deactivate (subscription expired, etc.):

```kotlin
LicenseManager.deactivateDevice(context)
// Notification will appear again
```

## MDM Integration

Your MDM system can control activation status by modifying the SharedPreference:
- Key: `license_prefs`
- Value: `is_activated` (true/false)

## Customization

Edit the notification text in `LicenseActivationService.kt`:

```kotlin
private fun showPersistentNotification() {
    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Your Custom Title")
        .setContentText("Your custom message")
        // ... rest of configuration
}
```

## Requirements

- Android API 21+
- AndroidX libraries
- Kotlin 1.5+

## Permissions

- `android.permission.RECEIVE_BOOT_COMPLETED` - For boot startup
- `android.permission.POST_NOTIFICATIONS` - For showing notifications (Android 13+)
- `android.permission.FOREGROUND_SERVICE` - For foreground service

## License

This code is provided as-is for your kiosk POS system.