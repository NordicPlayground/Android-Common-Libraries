package no.nordicsemi.android.common.test.scanner

import no.nordicsemi.android.common.navigation.asDestinations
import no.nordicsemi.android.common.navigation.createDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.ui.scanner.DeviceSelected
import no.nordicsemi.android.common.ui.scanner.ScannerScreen
import no.nordicsemi.android.common.ui.scanner.ScanningCancelled

val Scanner = createDestination("scanner")

private val ScannerDestination = defineDestination(Scanner) { navigator ->
    ScannerScreen(uuid = null) {
        when (it) {
            is DeviceSelected -> navigator.navigateUpWithResult(it.device)
            ScanningCancelled -> navigator.navigateUp()
        }
    }
}

val ScannerDestinations = ScannerDestination.asDestinations()