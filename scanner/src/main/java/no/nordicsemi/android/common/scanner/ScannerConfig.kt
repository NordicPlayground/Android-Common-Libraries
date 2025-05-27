package no.nordicsemi.android.common.scanner

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import no.nordicsemi.android.common.scanner.FilterConfig.Disabled
import no.nordicsemi.android.common.scanner.FilterConfig.Enabled
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Configuration for the filter settings in the scanner.
 * @param filterConfig The current state of the filter settings, which can be enabled or disabled.
 * @param scanWithServiceUuid The service UUID to filter the scan results, or None if no specific UUID is used.
 */
@Parcelize // TODO: Remove the Parcelable annotation when scanner destination is removed.
data class ScannerConfig(
    val filterConfig: FilterConfig,
    val scanWithServiceUuid: ScanWithServiceUuid
): Parcelable

/**
 * Represents the state of the filter settings in the scanner.
 * @property Disabled Indicates that the filter settings are disabled.
 * @property Enabled Contains the filter settings when they are enabled.
 */
@Parcelize
sealed class FilterConfig: Parcelable  {
    data object Disabled : FilterConfig()
    data class Enabled(val filter: FilterSettings) : FilterConfig()
}

/**
 * Data class representing the filter settings in the scanner view.
 * @property showNearby Indicates whether to show nearby settings.
 * @property showNonEmptyName Indicates whether to show devices with non-empty names setting.
 * @property showBonded Indicates whether to show bonded devices setting.
 * @property showSortByOption Indicates whether to show the sort by option.
 * @property showGroupByDropdown Indicates whether to show the group by dropdown.
 */
@Parcelize
data class FilterSettings(
    val showNearby: Boolean,
    val showNonEmptyName: Boolean,
    val showBonded: Boolean,
    val showSortByOption: Boolean,
    val showGroupByDropdown: Boolean,
): Parcelable  {
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
@Parcelize
sealed class ScanWithServiceUuid: Parcelable  {
    data object None : ScanWithServiceUuid()

    @OptIn(ExperimentalUuidApi::class)
    data class Specific(val serviceUuid: Uuid) : ScanWithServiceUuid()

}
