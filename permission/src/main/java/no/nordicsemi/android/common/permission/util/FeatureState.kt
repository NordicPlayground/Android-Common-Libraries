package no.nordicsemi.android.common.permission.util

enum class FeatureNotAvailableReason {
    PERMISSION_REQUIRED,
    NOT_AVAILABLE,
    DISABLED,
}

sealed class FeatureState

object Available : FeatureState()
data class NotAvailable(val reason: FeatureNotAvailableReason) : FeatureState()
