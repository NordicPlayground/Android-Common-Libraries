package no.nordicsemi.ui.scanner.navigation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import no.nordicsemi.ui.scanner.LocalDataProvider
import no.nordicsemi.ui.scanner.Utils

internal class ScannerNavigationViewModel(
    private val utils: Utils,
    private val dataProvider: LocalDataProvider
) : ViewModel() {

    val destination = MutableStateFlow(getNextScreenDestination())

    fun refreshNavigation() {
        val nextDestination = getNextScreenDestination()
        if (destination.value != nextDestination) {
            destination.value = nextDestination
        }
    }

    private fun getNextScreenDestination(): NavDestination {
        return when {
            !utils.isBluetoothAvailable() -> BluetoothNotAvailableDestination
            !utils.isLocationPermissionGranted() -> LocationPermissionRequiredDestination
            !utils.isBluetoothConnectPermissionGranted() -> BluetoothPermissionRequiredDestination
            !utils.isBleEnabled -> BluetoothDisabledDestination
            dataProvider.device == null -> PeripheralDeviceRequiredDestination
            else -> FinishDestination(dataProvider.device!!).also {
                dataProvider.device = null
            }
        }
    }
}
