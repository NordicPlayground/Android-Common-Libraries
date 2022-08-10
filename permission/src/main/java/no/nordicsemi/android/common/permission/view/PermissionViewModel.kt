package no.nordicsemi.android.common.permission.view

import android.app.Activity
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.nordicsemi.android.common.permission.PermissionManager
import no.nordicsemi.android.common.permission.manager.BluetoothPermissionResult
import no.nordicsemi.android.common.permission.manager.InternetPermissionResult
import no.nordicsemi.android.common.permission.manager.LocalDataProvider
import no.nordicsemi.android.common.permission.manager.PermissionUtils
import javax.inject.Inject

/**
 * Needed for injecting to @Composable functions.
 */
@HiltViewModel
class PermissionViewModel @Inject internal constructor(
    private val utils: PermissionUtils,
    private val dataProvider: LocalDataProvider,
    private val permissionManager: PermissionManager
) : ViewModel() {

    private val _bluetoothPermission = MutableStateFlow(BluetoothPermissionResult.BLUETOOTH_PERMISSION_REQUIRED)
    val bluetoothPermission = _bluetoothPermission.asStateFlow()

    private val _internetPermission = MutableStateFlow(InternetPermissionResult.INTERNET_DISABLED)
    val internetPermission = _internetPermission.asStateFlow()

    init {
        checkBluetooth()
        checkInternet()
    }

    val isLocationPermissionRequired = dataProvider.isLocationPermissionRequired

    fun isBluetoothScanPermissionDeniedForever(activity: Activity): Boolean {
        return utils.isBluetoothScanPermissionDeniedForever(activity)
    }

    fun isLocationPermissionDeniedForever(activity: Activity): Boolean {
        return utils.isLocationPermissionDeniedForever(activity)
    }

    fun checkBluetooth() {
        _bluetoothPermission.value = permissionManager.checkBluetooth()
    }

    fun checkInternet() {
        _internetPermission.value = permissionManager.checkInternet()
    }
}
