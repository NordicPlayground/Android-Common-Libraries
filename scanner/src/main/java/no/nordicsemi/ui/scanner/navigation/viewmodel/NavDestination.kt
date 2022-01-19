package no.nordicsemi.ui.scanner.navigation.viewmodel

internal sealed class NavDestination(val id: String)

internal object LocationPermissionRequiredDestination : NavDestination("location-permission-not-granted")
internal object BluetoothPermissionRequiredDestination : NavDestination("bluetooth-permission-not-granted")
internal object BluetoothNotAvailableDestination : NavDestination("bluetooth-not-available")
internal object BluetoothDisabledDestination : NavDestination("bluetooth-not-enabled")
internal object PeripheralDeviceRequiredDestination : NavDestination("device-not-selected")
