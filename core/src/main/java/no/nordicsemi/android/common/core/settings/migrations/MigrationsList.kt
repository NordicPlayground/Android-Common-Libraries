package no.nordicsemi.android.common.core.settings.migrations

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import no.nordicsemi.android.common.core.settings.NordicCommonLibsSettings
import java.io.File

private const val SHARED_PREFS_NAME_BLE_WIFI = "SHARED_PREFS_NAME"
private const val PREFS_PERMISSION_REQUESTED = "permission_requested"
private const val PREFS_BLUETOOTH_PERMISSION_REQUESTED = "bluetooth_permission_requested"
private const val PREFS_WIFI_PERMISSION_REQUESTED = "wifi_permission_requested"
private const val SHARED_PREFS_NAME_NOTIFICATIONS = "SHARED_PREFS_NOTIFICATION"
private const val PREFS_NOTIFICATION_PERMISSION_REQUESTED = "notification_permission_requested"
private const val DATASTORE_ANALYTICS_PREFERENCES_FILENAME = "ANALYTICS_PERMISSION"
private const val DATASTORE_ANALYTICS_HAS_BEEN_GRANTED = "HAS_BEEN_GRANTED"
private const val DATASTORE_ANALYTICS_WAS_INFO_SHOWN = "WAS_INFO_SHOWN"
private val Context.analyticsDataStore: DataStore<Preferences>
    by preferencesDataStore(name = DATASTORE_ANALYTICS_PREFERENCES_FILENAME)

fun getMigrationsList(context: Context): List<DataMigration<NordicCommonLibsSettings>> {
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
        },
        object : DataMigration<NordicCommonLibsSettings> {
            private val dataStoreFile =
                File(context.filesDir,
                    "datastore/$DATASTORE_ANALYTICS_PREFERENCES_FILENAME.preferences_pb"
                )

            override suspend fun shouldMigrate(currentData: NordicCommonLibsSettings): Boolean {
                return dataStoreFile.exists()
            }

            override suspend fun migrate(
                currentData: NordicCommonLibsSettings
            ): NordicCommonLibsSettings {
                // Access old Preferences DataStore
                val oldPreferences = context.analyticsDataStore.data.first()
                return currentData
                    .toBuilder()
                    .setAnalyticsWasInfoShown(
                        oldPreferences[booleanPreferencesKey(DATASTORE_ANALYTICS_WAS_INFO_SHOWN)]
                            ?: false
                    )
                    .setAnalyticsHasBeenGranted(
                        oldPreferences[booleanPreferencesKey(DATASTORE_ANALYTICS_HAS_BEEN_GRANTED)]
                            ?: false
                    )
                    .build()
            }

            override suspend fun cleanUp() {
                dataStoreFile.delete()
            }
        }
    )
}
