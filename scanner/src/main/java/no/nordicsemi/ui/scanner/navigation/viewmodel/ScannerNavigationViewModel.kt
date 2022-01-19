package no.nordicsemi.ui.scanner.navigation.viewmodel

import android.os.ParcelUuid
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import no.nordicsemi.android.navigation.NavigationManager
import no.nordicsemi.android.navigation.ParcelableArgument
import no.nordicsemi.android.navigation.UUIDArgument
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.ScannerDestinationId
import no.nordicsemi.ui.scanner.Utils
import no.nordicsemi.ui.scanner.permissions.DeviceSelected
import no.nordicsemi.ui.scanner.permissions.NavigateUp
import no.nordicsemi.ui.scanner.permissions.PermissionsViewEvent
import no.nordicsemi.ui.scanner.permissions.RefreshNavigation
import no.nordicsemi.ui.scanner.ui.exhaustive

internal class ScannerNavigationViewModel(
    private val utils: Utils,
    private val navigationManager: NavigationManager
) : ViewModel() {

    val args = navigationManager.getArgument(ScannerDestinationId) as UUIDArgument
    val filterId = ParcelUuid(args.value)
    val destination = MutableStateFlow(getNextScreenDestination())

    private var device: DiscoveredBluetoothDevice? = null

    fun onEvent(event: PermissionsViewEvent) {
        when (event) {
            NavigateUp -> navigationManager.navigateUp()
            RefreshNavigation -> refreshNavigation()
            is DeviceSelected -> onDeviceSelected(event.device)
        }.exhaustive
    }

    private fun onDeviceSelected(device: DiscoveredBluetoothDevice) {
        this.device = device
        refreshNavigation()
    }

    private fun refreshNavigation() {
        val nextDestination = getNextScreenDestination()

        if (nextDestination == null) {
            navigationManager.navigateUp(ParcelableArgument(device!!))
        } else if (destination.value != nextDestination) {
            destination.value = nextDestination
        }
    }

    private fun getNextScreenDestination(): NavDestination? {
        return when {
            !utils.isBluetoothAvailable() -> BluetoothNotAvailableDestination
            !utils.isLocationPermissionGranted() -> LocationPermissionRequiredDestination
            !utils.isBluetoothConnectPermissionGranted() -> BluetoothPermissionRequiredDestination
            !utils.isBleEnabled -> BluetoothDisabledDestination
            device == null -> PeripheralDeviceRequiredDestination
            else -> null
        }
    }
}
