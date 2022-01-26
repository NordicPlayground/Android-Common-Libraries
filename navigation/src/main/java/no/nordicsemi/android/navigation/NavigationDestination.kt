package no.nordicsemi.android.navigation

sealed class NavigationDestination

object InitialDestination : NavigationDestination()

object BackDestination : NavigationDestination()

data class ForwardDestination(val id: DestinationId) : NavigationDestination()
