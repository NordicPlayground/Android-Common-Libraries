package no.nordicsemi.android.common.permissions.internet.util

enum class InternetPermissionNotAvailableReason {
    NOT_AVAILABLE,
    DISABLED,
}

sealed class InternetPermissionState {

    object Available : InternetPermissionState()

    data class NotAvailable(
        val reason: InternetPermissionNotAvailableReason,
    ) : InternetPermissionState()
}