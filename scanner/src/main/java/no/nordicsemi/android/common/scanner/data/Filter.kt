package no.nordicsemi.android.common.scanner.data

import no.nordicsemi.kotlin.ble.client.android.ScanResult
import no.nordicsemi.kotlin.ble.core.BondState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed class Filter(
    open val title: String,
    open val filter: (ScanResult) -> Boolean
)

/**
 * Sort the scan result.
 * @param sortType The type of sorting to be applied.
 */
data class SortBy(
    val sortType: SortType,
    override val filter: (scanResult: ScanResult) -> Boolean = { true },
    override val title: String = when (sortType) {
        SortType.RSSI -> RSSI
        SortType.ALPHABETICAL -> ALPHABETICAL
    },
) : Filter(
    title = title,
    filter = filter
)

/**
 * Filter that allows scan results with no empty names.
 */
data class OnlyWithNames(
    override val title: String = ONLY_WITH_NAMES,
) : Filter(
    title = title,
    filter = { scanResult ->
        scanResult.peripheral.name?.isNotEmpty() == true
    }
)

/**
 * Group scan results by name.
 */
data class GroupByName(
    val name: String,
    val items: List<ScanResult>,
    override val title: String = GROUP_BY_NAME,
) : Filter(
    title = title,
    filter = { scanResult ->
        scanResult.peripheral.name == name
    }
)

/**
 * Filter bonded devices.
 * isBonded is true if the device is bonded, false otherwise.
 */
data class OnlyBonded(
    override val title: String = ONLY_BONDED,
) : Filter(
    title = title,
    filter = { scanResult ->
        scanResult.peripheral.bondState.value == BondState.BONDED
    }
)

/**
 * Filter nearby devices based on RSSI value. It will allow devices with RSSI value greater
 * or equal to  the -50 dBm.
 */
data class OnlyNearby(
    val rssi: Int = -50, // Default RSSI value to filter nearby devices
    override val title: String = ONLY_NEARBY
) : Filter(
    title = title,
    filter = { scanResult ->
        scanResult.rssi >= rssi
    }
)

@OptIn(ExperimentalUuidApi::class)
data class WithServiceUuid(
    val uuid: Uuid,
    override val title: String = WITH_SERVICE_UUID,
) : Filter(
    title = title,
    filter = { scanResult ->
        scanResult.advertisingData.serviceUuids.contains(uuid)
    }
)

/**
 * Custom filter.
 *
 * The filter shows only devices that match the given predicate.
 */
data class CustomFilter(
    override val title: String,
    override val filter: (scanResult: ScanResult) -> Boolean,
) : Filter(title, filter)

// TODO: Remove this later.
private const val RSSI = "RSSI"
const val ALPHABETICAL = "Alphabetical"
const val ONLY_WITH_NAMES = "Names"
const val GROUP_BY_NAME = "Group by name"
const val ONLY_BONDED = "Bonded"
const val ONLY_NEARBY = "Nearby"
const val WITH_SERVICE_UUID = "With service UUID"
