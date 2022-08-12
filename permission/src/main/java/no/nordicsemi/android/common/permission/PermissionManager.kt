package no.nordicsemi.android.common.permission

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow
import no.nordicsemi.android.common.permission.manager.BluetoothPermissionResult
import no.nordicsemi.android.common.permission.manager.InternetPermissionResult
import no.nordicsemi.android.common.permission.manager.PermissionResult

interface PermissionManager {

    val permissionResult: StateFlow<PermissionResult>

    val bluetoothPermission: StateFlow<BluetoothPermissionResult>

    val internetPermission: StateFlow<InternetPermissionResult>

    val isLocationPermissionRequired: StateFlow<Boolean>

    fun isBluetoothScanPermissionDeniedForever(activity: Activity): Boolean

    fun isLocationPermissionDeniedForever(activity: Activity): Boolean

    fun onDevicesDiscovered()

    fun refreshLocationState()

    fun checkBluetooth()

    fun checkInternet()

    fun refresh()
}
