package no.nordicsemi.android.common.scanner.data

import no.nordicsemi.kotlin.ble.client.android.ScanResult

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
 * Group scan results by name.
 */
data class GroupByName(
    val name: String,
    val items: List<ScanResult>
) : ScanResultFilter

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
        is GroupByName -> "Group by name"
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
