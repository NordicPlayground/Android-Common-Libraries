package no.nordicsemi.android.common.permissions.ble.permissionState

import no.nordicsemi.android.common.permissions.ble.BlePermissionNotAvailableReason

/**
 * The state of Bluetooth permission.
 */
internal sealed class BlePermissionState {
    /** Bluetooth permission is granted. */
    data object Available : BlePermissionState()
    /**
     * Bluetooth is not available.
     *
     * @param reason The reason why the BLE permission is not available.
     */
    data class NotAvailable(
        val reason: BlePermissionNotAvailableReason,
    ) : BlePermissionState()
}