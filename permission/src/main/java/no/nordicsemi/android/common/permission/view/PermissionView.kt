package no.nordicsemi.android.common.permission.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.permission.manager.BluetoothPermissionResult
import no.nordicsemi.android.common.permission.manager.InternetPermissionResult

@Composable
fun PermissionScreen(
    onNavigateBack: () -> Unit,
    Content: @Composable () -> Unit
) {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val bluetoothPermissionState = viewModel.bluetoothPermission.collectAsState().value
    val internetPermissionState = viewModel.internetPermission.collectAsState().value

    when (bluetoothPermissionState) {
        BluetoothPermissionResult.LOCATION_PERMISSION_REQUIRED -> LocationPermissionRequiredScreen(
            onNavigateBack
        ) {
            viewModel.refresh()
        }
        BluetoothPermissionResult.BLUETOOTH_PERMISSION_REQUIRED -> BluetoothPermissionRequiredScreen(
            onNavigateBack
        ) {
            viewModel.refresh()
        }
        BluetoothPermissionResult.BLUETOOTH_NOT_AVAILABLE -> BluetoothNotAvailableScreen(
            onNavigateBack
        )
        BluetoothPermissionResult.BLUETOOTH_DISABLED -> BluetoothDisabledScreen(onNavigateBack)
        BluetoothPermissionResult.ALL_GOOD -> when (internetPermissionState) {
            InternetPermissionResult.INTERNET_DISABLED -> InternetNotAvailableScreen(onNavigateBack)
            InternetPermissionResult.ALL_GOOD -> Content()
        }
    }
}

@Composable
fun BluetoothPermissionScreen(
    onNavigateBack: () -> Unit,
    Content: @Composable () -> Unit
) {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val bluetoothPermissionState = viewModel.bluetoothPermission.collectAsState().value

    when (bluetoothPermissionState) {
        BluetoothPermissionResult.LOCATION_PERMISSION_REQUIRED -> LocationPermissionRequiredScreen(
            onNavigateBack
        ) {
            viewModel.refresh()
        }
        BluetoothPermissionResult.BLUETOOTH_PERMISSION_REQUIRED -> BluetoothPermissionRequiredScreen(
            onNavigateBack
        ) {
            viewModel.refresh()
        }
        BluetoothPermissionResult.BLUETOOTH_NOT_AVAILABLE -> BluetoothNotAvailableScreen(
            onNavigateBack
        )
        BluetoothPermissionResult.BLUETOOTH_DISABLED -> BluetoothDisabledScreen(onNavigateBack)
        BluetoothPermissionResult.ALL_GOOD -> Content()
    }
}
