package no.nordicsemi.ui.scanner.navigation.viewmodel

import android.os.ParcelUuid
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import no.nordicsemi.android.navigation.*
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.ScannerDestinationId
import no.nordicsemi.ui.scanner.Utils
import no.nordicsemi.ui.scanner.permissions.DeviceSelected
import no.nordicsemi.ui.scanner.permissions.NavigateUp
import no.nordicsemi.ui.scanner.permissions.PermissionsViewEvent
import no.nordicsemi.ui.scanner.permissions.RefreshNavigation
import no.nordicsemi.ui.scanner.ui.exhaustive
import javax.inject.Inject

@HiltViewModel
internal class ScannerNavigationViewModel @Inject constructor(
    val utils: Utils,
    private val navigationManager: NavigationManager
) : ViewModel() {

    val args = navigationManager.getImmediateArgument(ScannerDestinationId) as UUIDArgument
    val filterId = ParcelUuid(args.value)
    val destination = MutableStateFlow(getNextScreenDestination())

    private var device: DiscoveredBluetoothDevice? = null

    fun onEvent(event: PermissionsViewEvent) {
        when (event) {
            NavigateUp -> navigationManager.navigateUp(ScannerDestinationId, CancelDestinationResult(ScannerDestinationId))
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
            navigationManager.navigateUp(
                ScannerDestinationId,
                SuccessDestinationResult(ScannerDestinationId, ParcelableArgument(ScannerDestinationId, device!!))
            )
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
