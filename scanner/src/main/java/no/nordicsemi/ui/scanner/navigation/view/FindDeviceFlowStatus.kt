package no.nordicsemi.ui.scanner.navigation.view

import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice

sealed class FindDeviceFlowStatus

data class FindDeviceSuccessResult(val device: DiscoveredBluetoothDevice) : FindDeviceFlowStatus()

object FindDeviceCloseResult : FindDeviceFlowStatus()
