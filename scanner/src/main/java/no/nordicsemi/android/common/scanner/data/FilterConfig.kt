package no.nordicsemi.android.common.scanner.data

internal sealed class FilterConfig {
    data object Disabled : FilterConfig()

    data class Enabled(val filter: FilterSettings) : FilterConfig()
}

internal data class FilterSettings(
    val showNearby: Boolean = false,
    val showNonEmptyName: Boolean = false,
    val showBonded: Boolean = false,
    val showSortByOption: Boolean = false,
    val showPeripheralDropdown: Boolean = false,
)
