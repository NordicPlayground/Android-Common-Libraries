package no.nordicsemi.android.navigation

data class ConsumableNavigationDestination(
    val destination: NavigationDestination,
    val isConsumed: Boolean = false
)
