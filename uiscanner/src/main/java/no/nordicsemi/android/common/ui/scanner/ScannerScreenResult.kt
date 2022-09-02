package no.nordicsemi.android.common.ui.scanner

import no.nordicsemi.android.common.ui.scanner.model.DiscoveredBluetoothDevice

sealed interface ScannerScreenResult

object ScanningCancelled : ScannerScreenResult

data class DeviceSelected(val device: DiscoveredBluetoothDevice) : ScannerScreenResult
