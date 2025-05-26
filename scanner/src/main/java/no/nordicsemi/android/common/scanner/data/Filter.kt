package no.nordicsemi.android.common.scanner.data

import no.nordicsemi.kotlin.ble.client.android.ScanResult

sealed interface Filter

/**
 * Sort the scan result.
 * @param sortType The type of sorting to be applied.
 */
data class SortBy(
    val sortType: SortType
) : Filter

/**
 * Filter that allows scan results with no empty names.
 */
data object OnlyWithNames : Filter

/**
 * Group scan results by name.
 */
data class GroupByName(
    val name: String,
    val items: List<ScanResult>
) : Filter

/**
 * Filter bonded devices.
 * isBonded is true if the device is bonded, false otherwise.
 */
data object OnlyBonded : Filter

/**
 * Filter nearby devices based on RSSI value. It will allow devices with RSSI value greater
 * or equal to  the -50 dBm.
 */
data object OnlyNearby : Filter

fun Filter.toDisplayTitle(): String {
    return when (this) {
        is OnlyWithNames -> "Name"
        is GroupByName -> "Group by name"
        is OnlyBonded -> "Bonded"
        is OnlyNearby -> "Nearby"

        is SortBy -> {
            when (sortType) {
                SortType.RSSI -> "RSSI"
                SortType.ALPHABETICAL -> "Alphabetical"
            }
        }
    }
}
