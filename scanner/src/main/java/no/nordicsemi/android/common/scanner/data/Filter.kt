package no.nordicsemi.android.common.scanner.data

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import no.nordicsemi.android.common.scanner.R
import no.nordicsemi.android.common.scanner.view.FilterButton
import no.nordicsemi.android.common.scanner.view.GroupByNameDropdown
import no.nordicsemi.android.common.scanner.view.SortByView
import no.nordicsemi.kotlin.ble.client.android.ScanResult
import no.nordicsemi.kotlin.ble.core.BondState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface Filter {
    val title: StringResource
    val filter: (ScanResult) -> Boolean
}

/**
 * Sort the scan result.
 * @param sortType The type of sorting to be applied.
 */
data class SortBy(
    val sortType: SortType? = null, // Nullable to allow for default sorting
    override val filter: (ScanResult) -> Boolean = { true } // Default filter that allows all scan results
) : Filter {
    override val title: StringResource
        get() = when (sortType) {
            SortType.RSSI -> StringResource.RSSI
            SortType.ALPHABETICAL -> StringResource.ALPHABETICAL
            null -> {
                // Default case when sortType is null, can be used for initial state or no sorting
                StringResource.RSSI // Default to RSSI sorting
            }
        }

    @Composable
    internal fun Draw(
        sortByFilters: List<SortBy> = listOf(
            SortBy(SortType.RSSI),
            SortBy(SortType.ALPHABETICAL)
        ),
        activeFilters: List<Filter>,
        onSortOptionSelected: (FilterEvent) -> Unit
    ) {
        SortByView(
            sortByFilters = sortByFilters,
            activeFilters = activeFilters,
            onSortOptionSelected = onSortOptionSelected
        )
    }
}


/**
 * Filter that allows scan results with no empty names.
 */
data class OnlyWithNames(
    override val title: StringResource = StringResource.ONLY_WITH_NAMES,
) : Filter {
    override val filter: (ScanResult) -> Boolean
        get() = { scanResult ->
            scanResult.peripheral.name?.isNotEmpty() == true
        }

    @SuppressLint("ComposableNaming")
    @Composable
    fun draw(
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        FilterButton(
            title = stringResource(id = title.value),
            isSelected = isSelected,
            onClick = onClick
        )
    }
}

/**
 * Group scan results by name.
 */
data class GroupByName(
    val name: String = "",
    override val title: StringResource = StringResource.GROUP_BY_NAME,
) : Filter {
    override val filter: (ScanResult) -> Boolean
        get() = { scanResult ->
            scanResult.peripheral.name == name
        }

    @Composable
    internal fun Draw(
        dropdownLabel: String,
        onLabelChange: (String) -> Unit,
        scanResults: List<ScanResult>,
        onItemSelected: (FilterEvent) -> Unit
    ) {
        GroupByNameDropdown(
            title = stringResource(title.value),
            dropdownLabel = dropdownLabel,
            onLabelChange = { onLabelChange(it) },
            scanResults = scanResults,
            onItemSelected = { onItemSelected(it) }
        )
    }
}

/**
 * Filter bonded devices.
 * isBonded is true if the device is bonded, false otherwise.
 */
data class OnlyBonded(
    override val title: StringResource = StringResource.ONLY_BONDED,
) : Filter {
    override val filter: (ScanResult) -> Boolean
        get() = { scanResult ->
            scanResult.peripheral.bondState.value == BondState.BONDED
        }

    @Composable
    internal fun Draw(
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        FilterButton(
            title = stringResource(id = title.value),
            isSelected = isSelected,
            onClick = onClick
        )
    }
}


/**
 * Filter nearby devices based on RSSI value. It will allow devices with RSSI value greater
 * or equal to  the -50 dBm.
 */
data class OnlyNearby(
    val rssi: Int = -50, // Default RSSI value to filter nearby devices
    override val title: StringResource = StringResource.ONLY_NEARBY,
) : Filter {
    override val filter: (ScanResult) -> Boolean
        get() = { scanResult ->
            scanResult.rssi >= rssi
        }

    @Composable
    internal fun Draw(
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        FilterButton(
            title = stringResource(id = title.value),
            isSelected = isSelected,
            onClick = onClick
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
data class WithServiceUuid(
    val uuid: Uuid?,
    override val title: StringResource = StringResource.WITH_SERVICE_UUID,
) : Filter {
    override val filter: (ScanResult) -> Boolean
        get() = { scanResult ->
            scanResult.advertisingData.serviceUuids.contains(uuid)
        }
}

/**
 * Custom filter.
 *
 * The filter shows only devices that match the given predicate.
 */
data class CustomFilter(
    override val title: StringResource,
    override val filter: (scanResult: ScanResult) -> Boolean,
) : Filter

/**
 * Enum class for Filter item string resources.
 */
enum class StringResource(@StringRes val value: Int) {
    ONLY_NEARBY(R.string.filter_only_nearby),
    ONLY_BONDED(R.string.filter_only_bonded),
    ONLY_WITH_NAMES(R.string.filter_only_with_names),
    GROUP_BY_NAME(R.string.filter_group_by_name),
    WITH_SERVICE_UUID(R.string.filter_with_service_uuid),
    RSSI(R.string.filter_rssi),
    ALPHABETICAL(R.string.filter_alphabetical),
}

