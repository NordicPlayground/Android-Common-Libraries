package no.nordicsemi.android.common.test.scanner

import android.os.ParcelUuid
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.navigation.createDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.navigation.viewmodel.SimpleNavigationViewModel
import no.nordicsemi.android.common.ui.scanner.DeviceSelected
import no.nordicsemi.android.common.ui.scanner.ScannerScreen
import no.nordicsemi.android.common.ui.scanner.ScanningCancelled
import no.nordicsemi.android.common.ui.scanner.model.DiscoveredBluetoothDevice

val Scanner = createDestination<ParcelUuid?, DiscoveredBluetoothDevice>("scanner")

val ScannerDestination = defineDestination(Scanner) {
    val vm: SimpleNavigationViewModel = hiltViewModel()
    val uuid = vm.nullableParameterOf(Scanner)

    ScannerScreen(
        uuid = uuid,
        onResult = { result ->
            when (result) {
                is DeviceSelected -> vm.navigateUpWithResult(Scanner, result.device)
                is ScanningCancelled -> vm.navigateUp()
            }
        }
    )
}