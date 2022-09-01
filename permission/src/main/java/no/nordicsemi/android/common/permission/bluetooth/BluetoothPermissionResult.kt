package no.nordicsemi.android.common.permission.bluetooth

enum class BluetoothPermissionResult {
    LOCATION_PERMISSION_REQUIRED,
    BLUETOOTH_PERMISSION_REQUIRED,
    BLUETOOTH_NOT_AVAILABLE,
    BLUETOOTH_DISABLED,
    ALL_GOOD
}