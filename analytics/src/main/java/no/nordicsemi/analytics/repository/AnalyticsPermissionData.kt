package no.nordicsemi.analytics.repository

data class AnalyticsPermissionData(
    val isPermissionGranted: Boolean = false,
    val wasInfoDialogShown: Boolean = false
)
