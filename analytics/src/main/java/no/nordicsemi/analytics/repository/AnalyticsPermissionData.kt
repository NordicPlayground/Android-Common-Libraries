package no.nordicsemi.analytics.repository

data class AnalyticsPermissionData(
    val isPermissionGranted: Boolean,
    val wasInfoDialogShown: Boolean
)
