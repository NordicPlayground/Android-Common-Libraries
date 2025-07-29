package no.nordicsemi.android.common.core.settings.migrations

import android.content.Context
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import no.nordicsemi.android.common.core.settings.NordicCommonLibsSettings

private const val SHARED_PREFS_NAME_BLE_WIFI = "SHARED_PREFS_NAME"
private const val PREFS_PERMISSION_REQUESTED = "permission_requested"
private const val PREFS_BLUETOOTH_PERMISSION_REQUESTED = "bluetooth_permission_requested"
private const val PREFS_WIFI_PERMISSION_REQUESTED = "wifi_permission_requested"
private const val SHARED_PREFS_NAME_NOTIFICATIONS = "SHARED_PREFS_NOTIFICATION"
private const val PREFS_NOTIFICATION_PERMISSION_REQUESTED = "notification_permission_requested"

fun getMigrationsList(context: Context): List<SharedPreferencesMigration<NordicCommonLibsSettings>> {
    return listOf(
        SharedPreferencesMigration(
            context,
            sharedPreferencesName = SHARED_PREFS_NAME_BLE_WIFI,
            keysToMigrate = setOf(
                PREFS_PERMISSION_REQUESTED,
                PREFS_BLUETOOTH_PERMISSION_REQUESTED,
                PREFS_WIFI_PERMISSION_REQUESTED
            )
        ) { sharedPrefs: SharedPreferencesView, currentData: NordicCommonLibsSettings ->
            // Define the mapping from SharedPreferences to Settings proto-datastore
            currentData
                .toBuilder()
                .setLocationPermissionRequested(
                    sharedPrefs
                        .getBoolean(PREFS_PERMISSION_REQUESTED, false)
                )
                .setBluetoothPermissionRequested(
                    sharedPrefs
                        .getBoolean(PREFS_BLUETOOTH_PERMISSION_REQUESTED, false)
                )
                .setWifiPermissionRequested(
                    sharedPrefs
                        .getBoolean(PREFS_WIFI_PERMISSION_REQUESTED, false)
                )
                .build()
        },
        SharedPreferencesMigration(
            context,
            sharedPreferencesName = SHARED_PREFS_NAME_NOTIFICATIONS,
            keysToMigrate = setOf(
                PREFS_NOTIFICATION_PERMISSION_REQUESTED
            )
        ) { sharedPrefs: SharedPreferencesView, currentData: NordicCommonLibsSettings ->
            // Define the mapping from SharedPreferences to Settings proto-datastore
            currentData
                .toBuilder()
                .setNotificationPermissionRequested(
                    sharedPrefs
                        .getBoolean(PREFS_NOTIFICATION_PERMISSION_REQUESTED, false)
                )
                .build()
        }
    )
}
