package no.nordicsemi.android.common.scanner.data

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface ScanResultFilter

/**
 * Filter that allows all scan results.
 */
data object AllowAllScanResultFilter : ScanResultFilter

/**
 * Filter that allows scan results with a specific name.
 *
 * @param name The name to filter by.
 */
data class AllowNameScanResultFilter(val name: String) : ScanResultFilter {
    override fun toString(): String {
        return "AllowNameScanResultFilter(name='$name')"
    }
}

/**
 * Filter that allows scan results with no empty names.
 */
data object AllowNonEmptyNameScanResultFilter : ScanResultFilter {
    override fun toString(): String {
        return "AllowNonEmptyNameScanResultFilter"
    }
}

/**
 * Filter that allows scan results with a specific UUID.
 *
 * @param uuid The UUID to filter by.
 */
data class AllowUuidScanResultFilter @OptIn(ExperimentalUuidApi::class) constructor(val uuid: Uuid) :
    ScanResultFilter {
    @OptIn(ExperimentalUuidApi::class)
    override fun toString(): String {
        return "AllowUuidScanResultFilter(uuid='$uuid.')"
    }
}

/**
 * Filter that allows scan results with a specific address.
 *
 * @param address The address to filter by.
 */
data class AllowAddressScanResultFilter(val address: String) : ScanResultFilter {
    override fun toString(): String {
        return "AllowAddressScanResultFilter(address='$address')"
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
        is AllowNameScanResultFilter -> "Display name"
        is AllowNonEmptyNameScanResultFilter -> "Non-empty name"
        is AllowUuidScanResultFilter -> "Service UUID"
        is AllowAddressScanResultFilter -> "Address"
        is AllowNameAndAddressScanResultFilter -> "Name and Address"
        is AllowBondedScanResultFilter -> "Bonded devices only"
        is AllowNearbyScanResultFilter -> "Nearby"
        else -> "Reset"
    }
}

enum class ScanResultFilterType {
    NAME,
    NON_EMPTY_NAME,
    UUID,
    ADDRESS,
    NAME_AND_ADDRESS,
    BONDED,
    ALL,
    NEARBY;

    override fun toString(): String {
        return when (this) {
            NAME -> "Name"
            NON_EMPTY_NAME -> "Non-empty name"
            UUID -> "Service UUID"
            ADDRESS -> "Address"
            NAME_AND_ADDRESS -> "Name and Address"
            BONDED -> "Bonded devices only"
            NEARBY -> "Nearby"
            ALL -> "No filter"
        }
    }

    companion object {
        fun listOfScanFilters(): List<ScanResultFilterType> = entries
    }
}
