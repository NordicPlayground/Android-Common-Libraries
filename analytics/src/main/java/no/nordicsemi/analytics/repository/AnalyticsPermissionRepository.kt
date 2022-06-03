package no.nordicsemi.analytics.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val FILE = "UART_CONFIGURATION"
private const val HAS_BEEN_GRANTED = "HAS_BEEN_GRANTED"
private const val WAS_INFO_SHOWN = "WAS_INFO_SHOWN"

@Singleton
class AnalyticsPermissionRepository @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = FILE)

    private val hasBeenGranted = booleanPreferencesKey(HAS_BEEN_GRANTED)
    private val wasInfoShown = booleanPreferencesKey(WAS_INFO_SHOWN)

    val permissionData = context.dataStore.data.map {
        AnalyticsPermissionData(
            it[hasBeenGranted] ?: false,
            it[wasInfoShown] ?: false
        )
    }

    suspend fun onPermissionGranted() {
        context.dataStore.edit {
            it[hasBeenGranted] = true
            it[wasInfoShown] = true
        }
    }

    suspend fun onPermissionDenied() {
        context.dataStore.edit {
            it[hasBeenGranted] = false
            it[wasInfoShown] = true
        }
    }
}
