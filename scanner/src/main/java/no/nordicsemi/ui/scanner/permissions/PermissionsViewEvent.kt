package no.nordicsemi.ui.scanner.permissions

import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice

internal sealed class PermissionsViewEvent

internal object RefreshNavigation : PermissionsViewEvent()

internal object NavigateUp : PermissionsViewEvent()

internal data class DeviceSelected(val device: DiscoveredBluetoothDevice) : PermissionsViewEvent()
