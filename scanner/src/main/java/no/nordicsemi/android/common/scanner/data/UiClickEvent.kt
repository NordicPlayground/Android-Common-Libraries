package no.nordicsemi.android.common.scanner.data

import no.nordicsemi.kotlin.ble.client.android.ScanResult


sealed interface UiClickEvent

data class OnScanResultSelected(
    val device: ScanResult
) : UiClickEvent

data object OnReloadScanResults : UiClickEvent

data class OnFilterSelected(
    val filter: ScanResultFilter
) : UiClickEvent