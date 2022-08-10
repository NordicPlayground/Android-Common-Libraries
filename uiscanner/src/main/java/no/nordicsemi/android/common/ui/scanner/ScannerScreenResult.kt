package no.nordicsemi.android.common.ui.scanner

import no.nordicsemi.android.common.ui.scanner.model.DiscoveredBluetoothDevice

sealed interface ScannerScreenResult

object ScannerResultCancel : ScannerScreenResult

data class ScannerResultSuccess(val device: DiscoveredBluetoothDevice) : ScannerScreenResult
