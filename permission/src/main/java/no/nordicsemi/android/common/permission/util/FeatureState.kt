package no.nordicsemi.android.common.permission.util

enum class FeatureNotAvailableReason {
    PERMISSION_REQUIRED,
    NOT_AVAILABLE,
    DISABLED,
}

internal sealed class FeatureState

internal object Available : FeatureState()
internal data class NotAvailable(val reason: FeatureNotAvailableReason) : FeatureState()