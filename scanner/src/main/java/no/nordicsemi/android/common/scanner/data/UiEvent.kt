package no.nordicsemi.android.common.scanner.data

/**
 * Represents the events that can occur in the scanner UI.
 */
sealed interface UiEvent

/**
 * Event triggered when the user requests to reload or refresh the scan results.
 */
data object OnReloadScanResults : UiEvent

/**
 * Base interface for UI events related to filtering actions.
 */
sealed interface FilterEvent : UiEvent

/**
 * Event triggered when the user selects a specific filter option.
 *
 * @property filter The selected filter option to apply to the scan results.
 */
data class OnFilterSelected(
    val filter: Filter
) : FilterEvent

/**
 * Event triggered when the user resets or clears all active filters.
 */
data object OnFilterReset : FilterEvent
