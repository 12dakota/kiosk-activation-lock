# Kiosk Device Activation Lock - Setup Guide

## Overview
This system creates a persistent notification that appears when a device is not activated. The notification stays in the notification tray and cannot be dismissed by the user until the device is activated (payment received).

## Features
- ✅ Persistent notification in notification tray
- ✅ Shows on device boot automatically
- ✅ Cannot be dismissed by users
- ✅ Survives device restarts
- ✅ Works in kiosk mode
- ✅ Simple activation/deactivation management
- ✅ Android O+ compatible

## Installation Steps

### 1. Add Files to Your Project
Copy the following files to your `src/main/kotlin/com/yourcompany/kiosklock/` directory:
- `LicenseActivationService.kt`
- `BootReceiver.kt`
- `NotificationChannelSetup.kt`
- `LicenseManager.kt`
- `MainActivity.kt`

### 2. Update AndroidManifest.xml
Add the permissions and receivers from the provided `AndroidManifest.xml` snippet:
```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

### 3. Update Your build.gradle
Ensure you have the correct dependencies:
```gradle
dependencies {
    implementation 'androidx.core:core:1.10.1'
    implementation 'androidx.appcompat:appcompat:1.6.1'
}
```

### 4. Initialize in Your Application Class or MainActivity
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    // Setup notification channel
    NotificationChannelSetup.createNotificationChannel(this)
    
    // Start service if not activated
    if (!LicenseManager.isDeviceActivated(this)) {
        val serviceIntent = Intent(this, LicenseActivationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }
}
```

## Usage

### Check if Device is Activated
```kotlin
val isActivated = LicenseManager.isDeviceActivated(context)
```

### Activate Device (After Payment)
```kotlin
LicenseManager.activateDevice(context)
// Notification will be removed automatically
```

### Deactivate Device (License Expired or Testing)
```kotlin
LicenseManager.deactivateDevice(context)
// Notification will reappear automatically
```

## Customization

### Change Notification Text
Edit `LicenseActivationService.kt` in the `showPersistentNotification()` method:
```kotlin
.setContentTitle("Your Custom Title")
.setContentText("Your custom message here")
```

### Change Notification Icon
Replace `android.R.drawable.ic_dialog_alert` with your custom icon:
```kotlin
.setSmallIcon(R.drawable.your_custom_icon)
```

### Add Action Buttons to Notification
```kotlin
val buyIntent = Intent(this, PaymentActivity::class.java)
val buyPendingIntent = PendingIntent.getActivity(this, 0, buyIntent, 
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

val notification = NotificationCompat.Builder(this, CHANNEL_ID)
    .setContentTitle("Device Not Active")
    .setContentText("Your device is not active. Please purchase an activation license.")
    .setSmallIcon(android.R.drawable.ic_dialog_alert)
    .setOngoing(true)
    .addAction(R.drawable.ic_buy, "Purchase License", buyPendingIntent)
    .build()
```

## How It Works

1. **On Device Boot**: `BootReceiver` receives `BOOT_COMPLETED` intent and starts `LicenseActivationService`

2. **Service Startup**: Service checks if device is activated via `LicenseManager`

3. **If Not Activated**: Service shows persistent foreground notification that cannot be dismissed

4. **On Payment**: Call `LicenseManager.activateDevice()` which:
   - Sets `is_activated` flag in SharedPreferences
   - Stops the service
   - Removes the notification

5. **On Deactivation**: Call `LicenseManager.deactivateDevice()` which:
   - Clears the activation flag
   - Restarts the service
   - Shows notification again

## Testing

### Test 1: Check Initial State
1. Install app on device
2. Grant notification permission when prompted
3. Open app - notification should appear in tray

### Test 2: Activate Device
1. From your payment processing screen, call `LicenseManager.activateDevice(context)`
2. Notification should disappear

### Test 3: Device Reboot
1. Reboot device
2. App should start automatically (depends on your kiosk setup)
3. If device is deactivated, notification should appear again

### Test 4: MDM Integration
Your MDM can now manage activation status by setting the SharedPreference `license_prefs` -> `is_activated` to `true`/`false`

## Integration with Your POS System

### From Your Payment Screen
```kotlin
class PaymentActivity : AppCompatActivity() {
    fun onPaymentConfirmed(paymentId: String) {
        // Process payment on your backend
        val isPaymentValid = verifyPaymentWithBackend(paymentId)
        
        if (isPaymentValid) {
            // Activate device
            LicenseManager.activateDevice(this)
            Toast.makeText(this, "Device activated!", Toast.LENGTH_SHORT).show()
        }
    }
}
```

### From Your MDM/Backend
Your MDM system can directly modify the SharedPreference through ADB or your management app:
```bash
adb shell am broadcast -a android.intent.action.BOOT_COMPLETED
```

## Troubleshooting

### Notification Not Showing
- Check notification permission is granted (Android 13+)
- Verify `NotificationChannelSetup.createNotificationChannel()` is called
- Check device isn't in Do Not Disturb mode

### Service Stops After Reboot
- Verify `BootReceiver` has `BOOT_COMPLETED` in manifest
- Check `android.permission.RECEIVE_BOOT_COMPLETED` is declared

### Notification Can Be Dismissed
- Ensure `setOngoing(true)` and `setAutoCancel(false)` are set
- Verify using foreground service

## Security Considerations

1. **Store activation data securely** - Consider encrypting SharedPreferences
2. **Verify with backend** - Don't trust client-side activation alone
3. **Use MDM for enforcement** - Combine with your MDM system for extra security
4. **Restrict package access** - Only your system app should modify `license_prefs`

## License

This code is provided as-is for your kiosk POS system.