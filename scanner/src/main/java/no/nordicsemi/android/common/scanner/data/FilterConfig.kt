package no.nordicsemi.android.common.scanner.data

import no.nordicsemi.android.common.scanner.data.FilterSettingsState.Disabled
import no.nordicsemi.android.common.scanner.data.FilterSettingsState.Enabled
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Configuration for the filter settings in the scanner.
 * @param filterSettings The current state of the filter settings, which can be enabled or disabled.
 * @param scanWithServiceUuid The service UUID to filter the scan results, or None if no specific UUID is used.
 */
internal data class FilterConfig(
    val filterSettings: FilterSettingsState = Disabled,
    val scanWithServiceUuid: ScanWithServiceUuid = ScanWithServiceUuid.None
)

/**
 * Represents the state of the filter settings in the scanner.
 * @property Disabled Indicates that the filter settings are disabled.
 * @property Enabled Contains the filter settings when they are enabled.
 */
internal sealed class FilterSettingsState {
    data object Disabled : FilterSettingsState()
    data class Enabled(val filter: FilterSettings) : FilterSettingsState()
}

/**
 * Data class representing the filter settings in the scanner view.
 * @property showNearby Indicates whether to show nearby settings.
 * @property showNonEmptyName Indicates whether to show devices with non-empty names setting.
 * @property showBonded Indicates whether to show bonded devices setting.
 * @property showSortByOption Indicates whether to show the sort by option.
 * @property showGroupByDropdown Indicates whether to show the group by dropdown.
 */
internal data class FilterSettings(
    val showNearby: Boolean = false,
    val showNonEmptyName: Boolean = false,
    val showBonded: Boolean = false,
    val showSortByOption: Boolean = false,
    val showGroupByDropdown: Boolean = false,
) {
    companion object {
        val Default = FilterSettings(
            showNearby = true,
            showNonEmptyName = true,
            showBonded = true,
            showSortByOption = true,
            showGroupByDropdown = true
        )
    }
}

/**
 * Represents the configuration for scanning with a specific service UUID.
 * This can either be set to None, indicating no specific UUID is used,
 * or to a Specific UUID, which filters the scan results based on the provided service UUID.
 */
internal sealed class ScanWithServiceUuid {
    data object None : ScanWithServiceUuid()

    @OptIn(ExperimentalUuidApi::class)
    data class Specific(val serviceUuid: Uuid) : ScanWithServiceUuid()

}
