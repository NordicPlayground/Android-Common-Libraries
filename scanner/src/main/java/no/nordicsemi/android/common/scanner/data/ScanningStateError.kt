package no.nordicsemi.android.common.scanner.data

import android.bluetooth.le.ScanCallback
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Reason for failed scan.
 *
 * @property value Native Android API value.
 */
enum class ScanFailedError(val value: Int) {

    /**
     * Fails to start scan as BLE is disabled on the device.
     */
    BLUETOOTH_DISABLED(-1),

    /**
     * Helper value representing an unknown error.
     */
    UNKNOWN(0),

    /**
     * Fails to start scan as BLE scan with the same settings is already started by the app.
     */
    SCAN_FAILED_ALREADY_STARTED(ScanCallback.SCAN_FAILED_ALREADY_STARTED),

    /**
     * Fails to start scan as app cannot be registered.
     */
    SCAN_FAILED_APPLICATION_REGISTRATION_FAILED(ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED),

    /**
     * Fails to start power optimized scan as this feature is not supported.
     */
    SCAN_FAILED_FEATURE_UNSUPPORTED(ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED),

    /**
     * Fails to start scan due an internal error.
     */
    SCAN_FAILED_INTERNAL_ERROR(ScanCallback.SCAN_FAILED_INTERNAL_ERROR),

    /**
     * Fails to start scan as it is out of hardware resources.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES(ScanCallback.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES),

    /**
     * Fails to start scan as application tries to scan too frequently.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    SCAN_FAILED_SCANNING_TOO_FREQUENTLY(ScanCallback.SCAN_FAILED_SCANNING_TOO_FREQUENTLY);

    companion object {
        fun create(value: Int): ScanFailedError {
            return entries.firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}

/**
 * Exception indicating that BLE scanning failed.
 *
 * @property errorCode A detailed error code.
 */
data class ScanningFailedException(
    val errorCode: ScanFailedError,
) : Exception("Scanning failed with the code: $errorCode")