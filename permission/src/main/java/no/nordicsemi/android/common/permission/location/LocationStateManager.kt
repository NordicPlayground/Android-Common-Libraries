package no.nordicsemi.android.common.permission.location

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.common.permission.util.*
import javax.inject.Inject
import javax.inject.Singleton

private const val REFRESH_PERMISSIONS = "no.nordicsemi.android.common.permission.REFRESH_LOCATION_PERMISSIONS"

@Singleton
internal class LocationStateManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataProvider = LocalDataProvider(context)
    private val utils = PermissionUtils(context, dataProvider)

    fun locationState() = callbackFlow {
        trySend(getLocationState())

        val locationStateChangeHandler = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                trySend(getLocationState())
            }
        }
        val filter = IntentFilter().apply {
            addAction(LocationManager.MODE_CHANGED_ACTION)
            addAction(REFRESH_PERMISSIONS)
        }
        context.registerReceiver(locationStateChangeHandler, filter)
        awaitClose {
            context.unregisterReceiver(locationStateChangeHandler)
        }
    }

    fun refreshPermission() {
        val intent = Intent(REFRESH_PERMISSIONS)
        context.sendBroadcast(intent)
    }

    fun markLocationPermissionRequested() {
        dataProvider.locationPermissionRequested = true
    }

    fun isLocationPermissionDeniedForever(activity: Activity): Boolean {
        return utils.isLocationPermissionDeniedForever(activity)
    }

    private fun getLocationState(): FeatureState {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return when {
            !utils.isLocationPermissionGranted ->
                NotAvailable(FeatureNotAvailableReason.PERMISSION_REQUIRED)
            dataProvider.isLocationPermissionRequired && !LocationManagerCompat.isLocationEnabled(lm) ->
                NotAvailable(FeatureNotAvailableReason.DISABLED)
            else -> Available
        }
    }
}