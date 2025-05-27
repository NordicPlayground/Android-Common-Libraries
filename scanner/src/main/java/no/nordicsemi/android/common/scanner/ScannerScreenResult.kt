package no.nordicsemi.android.common.scanner

import no.nordicsemi.kotlin.ble.client.android.ScanResult

/**
 * Represents the result of the scanner screen interaction.
 * This sealed interface defines the possible outcomes when the user interacts with the scanner screen.
 */
sealed interface ScannerScreenResult

/**
 * Represents the cancellation of the scanning process.
 * This object is used to indicate that the user has cancelled the scanning operation.
 */
data object ScanningCancelled : ScannerScreenResult

/**
 * Represents the selection of a device from the scan results.
 * @property scanResult The selected scan result containing details about the device.
 */
data class DeviceSelected(val scanResult: ScanResult) : ScannerScreenResult
