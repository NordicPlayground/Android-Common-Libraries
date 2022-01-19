package no.nordicsemi.ui.scanner

import no.nordicsemi.android.navigation.ComposeDestination
import no.nordicsemi.android.navigation.ComposeDestinations
import no.nordicsemi.android.navigation.DestinationId
import no.nordicsemi.ui.scanner.navigation.view.FindDeviceScreen

val ScannerDestinationId = DestinationId("scanner-destination")

private val ScannerDestination = ComposeDestination(ScannerDestinationId) { FindDeviceScreen() }

val ScannerDestinations = ComposeDestinations(listOf(ScannerDestination))