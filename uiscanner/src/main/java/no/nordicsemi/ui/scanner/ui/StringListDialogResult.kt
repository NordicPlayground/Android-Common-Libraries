package no.nordicsemi.ui.scanner.ui

import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice

sealed class StringListDialogResult

data class ItemSelectedResult(val value: DiscoveredBluetoothDevice): StringListDialogResult()

object FlowCanceled : StringListDialogResult()
