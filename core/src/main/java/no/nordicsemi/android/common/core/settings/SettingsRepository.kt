package no.nordicsemi.android.common.core.settings

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException

class SettingsRepository(private val context: Context) {
    private val dataStore = context.nordicSettingsDataStore
    val nordicSettings: Flow<Settings> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading $DATASTORE_FILENAME.", exception)
                emit(Settings.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun updateLocationPermissionRequested(requested: Boolean) {
        dataStore.updateData { settings ->
            settings
                .toBuilder()
                .setLocationPermissionRequested(requested)
                .build()
        }
    }

    suspend fun updateBluetoothPermissionsRequested(requested: Boolean) {
        dataStore.updateData { settings ->
            settings
                .toBuilder()
                .setBluetoothPermissionRequested(requested)
                .build()
        }
    }

    suspend fun updateWifiPermissionRequested(requested: Boolean) {
        dataStore.updateData { settings ->
            settings
                .toBuilder()
                .setWifiPermissionRequested(requested)
                .build()
        }
    }

    suspend fun updateNotificationPermissionRequested(requested: Boolean) {
        dataStore.updateData { settings ->
            settings
                .toBuilder()
                .setNotificationPermissionRequested(requested)
                .build()
        }
    }

    suspend fun fetchInitialSettings() = dataStore.data.first()

    companion object {
        private const val TAG: String = "NordicSettingsRepo"
    }
}