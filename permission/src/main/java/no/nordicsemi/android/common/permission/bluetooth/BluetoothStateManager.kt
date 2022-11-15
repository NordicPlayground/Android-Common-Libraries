package no.nordicsemi.android.common.permission.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.common.permission.util.*
import no.nordicsemi.android.common.permission.util.Available
import no.nordicsemi.android.common.permission.util.LocalDataProvider
import no.nordicsemi.android.common.permission.util.NotAvailable
import no.nordicsemi.android.common.permission.util.PermissionUtils
import javax.inject.Inject
import javax.inject.Singleton

private const val REFRESH_PERMISSIONS = "no.nordicsemi.android.common.permission.REFRESH_BLUETOOTH_PERMISSIONS"

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
        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(REFRESH_PERMISSIONS)
        }
        context.registerReceiver(bluetoothStateChangeHandler, filter)
        awaitClose {
            context.unregisterReceiver(bluetoothStateChangeHandler)
        }
    }

    fun refreshPermission() {
        val intent = Intent(REFRESH_PERMISSIONS)
        context.sendBroadcast(intent)
    }

    fun markBluetoothPermissionRequested() {
        dataProvider.bluetoothPermissionRequested = true
    }

    fun isBluetoothScanPermissionDeniedForever(activity: Activity): Boolean {
        return utils.isBluetoothScanPermissionDeniedForever(activity)
    }

    private fun getBluetoothPermissionState() = when {
        !utils.isBluetoothAvailable -> NotAvailable(FeatureNotAvailableReason.NOT_AVAILABLE)
        !utils.areNecessaryBluetoothPermissionsGranted -> NotAvailable(FeatureNotAvailableReason.PERMISSION_REQUIRED)
        !utils.isBleEnabled -> NotAvailable(FeatureNotAvailableReason.DISABLED)
        else -> Available
    }
}
