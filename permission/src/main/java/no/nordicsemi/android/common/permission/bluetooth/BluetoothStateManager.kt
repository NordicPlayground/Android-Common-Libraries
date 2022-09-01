package no.nordicsemi.android.common.permission.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.common.permission.LocalDataProvider
import no.nordicsemi.android.common.permission.PermissionUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothStateManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataProvider = LocalDataProvider(context)
    private val utils = PermissionUtils(context, dataProvider)

    fun bluetoothState() = callbackFlow {
        trySend(getBluetoothPermissionState())

        val bluetoothStateChangeHandler = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                trySend(getBluetoothPermissionState())
            }
        }
        context.registerReceiver(bluetoothStateChangeHandler, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
        awaitClose {
            context.unregisterReceiver(bluetoothStateChangeHandler)
        }
    }

    fun locationState() = callbackFlow {
        trySend(isLocationRequired())

        val locationStateChangeHandler = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                trySend(isLocationRequired())
            }
        }
        context.registerReceiver(locationStateChangeHandler, IntentFilter(LocationManager.MODE_CHANGED_ACTION))
        awaitClose {
            context.unregisterReceiver(locationStateChangeHandler)
        }
    }

    fun markLocationPermissionRequested() {
        dataProvider.locationPermissionRequested = true
    }

    fun markBluetoothPermissionRequested() {
        dataProvider.bluetoothPermissionRequested = true
    }

    fun isBluetoothScanPermissionDeniedForever(activity: Activity): Boolean {
        return utils.isBluetoothScanPermissionDeniedForever(activity)
    }

    fun isLocationPermissionDeniedForever(activity: Activity): Boolean {
        return utils.isLocationPermissionDeniedForever(activity)
    }

    private fun getBluetoothPermissionState() = when {
        !utils.isBluetoothAvailable -> BluetoothPermissionResult.BLUETOOTH_NOT_AVAILABLE
        !utils.isBleEnabled -> BluetoothPermissionResult.BLUETOOTH_DISABLED
        !utils.isLocationPermissionGranted -> BluetoothPermissionResult.LOCATION_PERMISSION_REQUIRED
        !utils.areNecessaryBluetoothPermissionsGranted -> BluetoothPermissionResult.BLUETOOTH_PERMISSION_REQUIRED
        else -> BluetoothPermissionResult.ALL_GOOD
    }

    private fun isLocationRequired(): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return dataProvider.isLocationPermissionRequired && !LocationManagerCompat.isLocationEnabled(lm)
    }
}