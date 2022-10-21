package no.nordicsemi.android.common.test.scanner

import android.os.ParcelUuid
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import no.nordicsemi.android.common.navigation.*
import no.nordicsemi.android.common.ui.scanner.DeviceSelected
import no.nordicsemi.android.common.ui.scanner.ScannerScreen
import no.nordicsemi.android.common.ui.scanner.ScannerScreenResult
import no.nordicsemi.android.common.ui.scanner.ScanningCancelled
import no.nordicsemi.android.common.ui.scanner.model.DiscoveredBluetoothDevice
import javax.inject.Inject

val Scanner = createDestination<ParcelUuid, DiscoveredBluetoothDevice>("scanner")

private val ScannerDestination = defineDestination(Scanner) {
    val vm = hiltViewModel<ScannerViewModel>()
    val uuid by vm.uuid.collectAsState()

    ScannerScreen(uuid = uuid) {
        vm.onEvent(it)
    }
}

val ScannerDestinations = ScannerDestination.asDestinations()

@HiltViewModel
private class ScannerViewModel @Inject constructor(
    private val navigator: Navigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val uuid = savedStateHandle.getStateFlow(Scanner, null)

    fun onEvent(event: ScannerScreenResult) {
        when (event) {
            is DeviceSelected -> navigator.navigateUpWithResult(Scanner, event.device)
            ScanningCancelled -> navigator.navigateUp()
        }
    }
}