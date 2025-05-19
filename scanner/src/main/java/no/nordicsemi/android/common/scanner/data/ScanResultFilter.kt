package no.nordicsemi.android.common.scanner.data

sealed interface ScanResultFilter

/**
 * Sort the scan result.
 */
data class SortScanResult(
    val sortType: SortType
) : ScanResultFilter

/**
 * Filter that allows scan results with no empty names.
 */
data object AllowNonEmptyNameScanResultFilter : ScanResultFilter {
    override fun toString(): String {
        return "AllowNonEmptyNameScanResultFilter"
    }
}

/**
 * Filter that allows scan results with a specific name and address.
 *
 * @param name The name to filter by.
 * @param address The address to filter by.
 */
data class AllowNameAndAddressScanResultFilter(
    val name: String,
    val address: String
) : ScanResultFilter {
    override fun toString(): String {
        return "AllowNameAndAddressScanResultFilter(name='$name', address='$address')"
    }
}

/**
 * Filter bonded devices.
 * isBonded is true if the device is bonded, false otherwise.
 */
data object AllowBondedScanResultFilter : ScanResultFilter

/**
 * Filter nearby devices based on RSSI value. It will allow devices with RSSI value greater
 * or equal to  the -50 dBm.
 */
data object AllowNearbyScanResultFilter : ScanResultFilter {
    override fun toString(): String {
        return "AllowNearbyScanResultFilter"
    }
}

fun ScanResultFilter.toDisplayTitle(): String {
    return when (this) {
        is AllowNonEmptyNameScanResultFilter -> "Name"
        is AllowNameAndAddressScanResultFilter -> "Name and Address"
        is AllowBondedScanResultFilter -> "Bonded"
        is AllowNearbyScanResultFilter -> "Nearby"

        is SortScanResult -> {
            when (sortType) {
                SortType.RSSI -> "RSSI"
                SortType.ALPHABETICAL -> "Alphabetical"
            }
        }
    }
}
