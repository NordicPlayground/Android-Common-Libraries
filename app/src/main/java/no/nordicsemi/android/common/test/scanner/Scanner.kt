package no.nordicsemi.android.common.test.scanner

import android.os.ParcelUuid
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.navigation.createDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.navigation.viewmodel.SimpleNavigationViewModel
import no.nordicsemi.android.common.ui.scanner.ScannerView
import no.nordicsemi.android.common.ui.scanner.model.DiscoveredBluetoothDevice

/**
 * The scanner destination servers as an example of a destination that takes a parameter and returns
 * a result. The parameter is a [ParcelUuid] that is used to filter the devices that are displayed.
 * The result is a [DiscoveredBluetoothDevice] that was selected by the user.
 */
val Scanner = createDestination<ParcelUuid?, DiscoveredBluetoothDevice>("scanner")

val ScannerDestination = defineDestination(Scanner) {
    val vm: SimpleNavigationViewModel = hiltViewModel()

    // Here's how the Composable can obtain the parameter passed to the destination.
    // As the type of the parameter can be null, so we need to use nullableParameterOf.
    val uuid = vm.nullableParameterOf(Scanner)

    ScannerView(
        uuid = uuid,
        onResult = { vm.navigateUpWithResult(Scanner, it) }
    )
}