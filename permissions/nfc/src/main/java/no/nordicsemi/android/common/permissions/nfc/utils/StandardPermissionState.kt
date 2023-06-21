package no.nordicsemi.android.common.permissions.nfc.utils

enum class StandardPermissionNotAvailableReason {
    NOT_AVAILABLE,
    DISABLED,
}

sealed class StandardPermissionState {
    object Available : StandardPermissionState()
    data class NotAvailable(val reason: StandardPermissionNotAvailableReason) :
        StandardPermissionState()
}