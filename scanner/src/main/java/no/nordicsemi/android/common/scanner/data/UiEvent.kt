package no.nordicsemi.android.common.scanner.data

import no.nordicsemi.kotlin.ble.client.android.ScanResult

/**
 * Represents the events that can occur in the scanner UI.
 */
sealed interface UiEvent

/**
 * Event triggered when a scan result (e.g., a Bluetooth device) is selected by the user.
 *
 * @property device The selected scan result.
 */
data class OnScanResultSelected(
    val device: ScanResult
) : UiEvent

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
