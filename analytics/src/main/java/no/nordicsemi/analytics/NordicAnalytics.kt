package no.nordicsemi.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.Size
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import no.nordicsemi.analytics.repository.AnalyticsPermissionRepository
import javax.inject.Inject
import javax.inject.Singleton

private const val LOG_TAG = "ANALYTICS"

@Singleton
class NordicAnalytics @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val repository: AnalyticsPermissionRepository
) {

    private val firebase by lazy { FirebaseAnalytics.getInstance(context) }

    fun logEvent(@Size(min = 1L,max = 40L) name: String, params: Bundle?) {
        runBlocking {
            repository.permissionData.firstOrNull()
                ?.takeIf { it.isPermissionGranted }
                ?.runCatching {
                    Log.d(LOG_TAG, "name: $name, params: $params")
                    firebase.logEvent(name, params)
                }
        }
    }
}
