package no.nordicsemi.android.common.core.settings

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import no.nordicsemi.android.common.core.settings.migrations.getMigrationsList
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val DATASTORE_FILENAME = "nordic_common_libs_settings.pb"
val Context.nordicCommonLibsSettingsDataStore: DataStore<NordicCommonLibsSettings> by
dataStore (
    fileName = DATASTORE_FILENAME,
    serializer = NordicCommonLibsSettingsSerializer,
    produceMigrations = { context -> getMigrationsList(context) }
)

@Singleton
class NordicCommonLibsSettingsRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore by lazy {
        context.nordicCommonLibsSettingsDataStore
    }
    val nordicCommonLibsSettings: Flow<NordicCommonLibsSettings> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading $DATASTORE_FILENAME.", exception)
                emit(NordicCommonLibsSettings.getDefaultInstance())
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

    suspend fun updateBluetoothPermissionRequested(requested: Boolean) {
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