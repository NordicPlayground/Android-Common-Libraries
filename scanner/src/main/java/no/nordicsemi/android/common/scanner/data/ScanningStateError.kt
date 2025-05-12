package no.nordicsemi.android.common.scanner.data

enum class ScanningStateError (val errorCode: Int){
    /** No error. */
    NO_ERROR(0),

    /** Bluetooth is not supported on this device. */
    BLUETOOTH_NOT_SUPPORTED(1),

    /** Bluetooth is disabled. */
    BLUETOOTH_DISABLED(2),

    /** Location permission is not granted. */
    LOCATION_PERMISSION_NOT_GRANTED(3),

    /** Location services are disabled. */
    LOCATION_SERVICES_DISABLED(4),

    /** Scanning is already in progress. */
    SCANNING_IN_PROGRESS(5),

    /** Scanning has been stopped. */
    SCANNING_STOPPED(6);

    companion object {
        fun fromErrorCode(errorCode: Int): ScanningStateError {
            return entries.firstOrNull { it.errorCode == errorCode } ?: NO_ERROR
        }
    }
}