package no.nordicsemi.android.common.permissions.nfc.utils

sealed class NfcPermissionState {
    object Available : NfcPermissionState()
    data class NotAvailable(val reason: NfcNotAvailableReason) :
        NfcPermissionState()
}

enum class NfcNotAvailableReason {
    NOT_AVAILABLE,
    DISABLED,
}