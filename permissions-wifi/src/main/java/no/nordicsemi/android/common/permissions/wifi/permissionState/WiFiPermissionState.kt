package no.nordicsemi.android.common.permissions.wifi.permissionState

import no.nordicsemi.android.common.permissions.wifi.WiFiPermissionNotAvailableReason

/**
 * Represents the state of Wi-Fi permission.
 */
internal sealed class WiFiPermissionState {

    /**
     * Represents the Wi-Fi permission is available.
     */
    data object Available : WiFiPermissionState()

    /**
     * Represents the Wi-Fi permission is not available.
     * @param reason The reason for Wi-Fi permission is not available.
     */
    data class NotAvailable(
        val reason: WiFiPermissionNotAvailableReason,
    ) : WiFiPermissionState()
}