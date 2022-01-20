package no.nordicsemi.android.navigation

private var navigationCounter = 0

data class NavigationDestinationComposeHelper(
    val destination: NavigationDestination,
    val id: Int = navigationCounter++ //Required different to trigger next LaunchedEffect
)
