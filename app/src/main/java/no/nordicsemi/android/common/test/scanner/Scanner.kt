package no.nordicsemi.android.common.test.scanner

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import no.nordicsemi.android.common.navigation.Navigator
import no.nordicsemi.android.common.navigation.asDestinations
import no.nordicsemi.android.common.navigation.createDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.ui.scanner.DeviceSelected
import no.nordicsemi.android.common.ui.scanner.ScannerScreen
import no.nordicsemi.android.common.ui.scanner.ScannerScreenResult
import no.nordicsemi.android.common.ui.scanner.ScanningCancelled
import javax.inject.Inject

val Scanner = createDestination("scanner")

private val ScannerDestination = defineDestination(Scanner) {
    val vm = hiltViewModel<ScannerViewModel>()

    ScannerScreen(uuid = null) {
        vm.onEvent(it)
    }
}

val ScannerDestinations = ScannerDestination.asDestinations()

@HiltViewModel
private class ScannerViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel() {

    fun onEvent(event: ScannerScreenResult) {
        when (event) {
            is DeviceSelected -> navigator.navigateUpWithResult(event.device)
            ScanningCancelled -> navigator.navigateUp()
        }
    }
}