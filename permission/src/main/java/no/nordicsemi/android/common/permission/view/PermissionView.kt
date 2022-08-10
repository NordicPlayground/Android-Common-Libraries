package no.nordicsemi.android.common.permission.view

import androidx.compose.runtime.Composable
import no.nordicsemi.android.common.permission.manager.BluetoothPermissionResult
import no.nordicsemi.android.common.permission.manager.InternetPermissionResult
import no.nordicsemi.android.common.permission.manager.PermissionResult

@Composable
fun PermissionView(permissionResult: PermissionResult, onPermissionChanged: () -> Unit) {
    when (permissionResult) {
        BluetoothPermissionResult.LOCATION_PERMISSION_REQUIRED -> LocationPermissionRequiredView(
            onPermissionChanged
        )
        BluetoothPermissionResult.BLUETOOTH_PERMISSION_REQUIRED -> BluetoothPermissionRequiredView(
            onPermissionChanged
        )
        BluetoothPermissionResult.BLUETOOTH_NOT_AVAILABLE -> BluetoothNotAvailableView()
        BluetoothPermissionResult.BLUETOOTH_DISABLED -> BluetoothDisabledView()
        InternetPermissionResult.INTERNET_DISABLED -> TODO()
        BluetoothPermissionResult.ALL_GOOD,
        InternetPermissionResult.ALL_GOOD -> throw IllegalArgumentException("Please Mister, all is good, I don't know what to draw here.")
    }
}
