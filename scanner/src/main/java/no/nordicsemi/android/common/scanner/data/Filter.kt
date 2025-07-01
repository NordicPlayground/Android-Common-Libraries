@file:Suppress("unused")

package no.nordicsemi.android.common.scanner.data

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.ui.graphics.vector.ImageVector
import no.nordicsemi.android.common.scanner.R
import no.nordicsemi.kotlin.ble.client.android.ScanResult
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed class Filter(
    @StringRes val title: Int,
    val icon: ImageVector,
    val isInitiallySelected: Boolean,
    val predicate: (selected: Boolean, result: ScanResult, highestRssi: Int) -> Boolean,
)

/**
 * Filter that allows scan results with no empty names.
 */
class OnlyWithNames(
    @StringRes title: Int = R.string.filter_only_with_names,
    isInitiallySelected: Boolean = false,
) : Filter(
    title = title,
    icon = Icons.AutoMirrored.Default.Label,
    isInitiallySelected = isInitiallySelected,
    predicate = { selected, result, _ ->
        !selected || result.peripheral.name?.isNotEmpty() == true
    },
)

/**
 * Filter nearby devices based on RSSI value.
 *
 * It will allow devices with RSSI value greater or equal to the given RSSI value.
 */
class OnlyNearby(
    rssi: Int = -50, // Default RSSI value to filter nearby devices
    @StringRes title: Int = R.string.filter_only_nearby,
    isInitiallySelected: Boolean = false,
) : Filter(
    title = title,
    icon = Icons.Default.MyLocation,
    isInitiallySelected = isInitiallySelected,
    predicate = { selected, _, highestRssi ->
        !selected || highestRssi >= rssi
    }
)

@OptIn(ExperimentalUuidApi::class)
class WithServiceUuid(
    uuid: Uuid,
    icon: ImageVector = Icons.Default.Check,
    @StringRes title: Int = R.string.filter_with_service_uuid,
    isInitiallySelected: Boolean = false,
) : Filter(
    title = title,
    icon = icon,
    isInitiallySelected = isInitiallySelected,
    predicate = { selected, result, _ ->
        !selected ||
        result.advertisingData.serviceUuids.contains(uuid) ||
        result.advertisingData.serviceData.keys.any { it == uuid } ||
        result.advertisingData.serviceSolicitationUuids.contains(uuid)
    }
)

/**
 * Custom filter.
 *
 * The filter shows only devices that match the given predicate.
 */
class CustomFilter(
    @StringRes title: Int,
    icon: ImageVector = Icons.Default.Check,
    isInitiallySelected: Boolean = false,
    predicate:  (selected: Boolean, result: ScanResult, highestRssi: Int) -> Boolean,
) : Filter(
    title = title,
    icon = icon,
    isInitiallySelected = isInitiallySelected,
    predicate = predicate
)

