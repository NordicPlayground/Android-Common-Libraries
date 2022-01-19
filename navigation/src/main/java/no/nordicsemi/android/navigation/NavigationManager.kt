package no.nordicsemi.android.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {

    private val _navigationDestination = MutableStateFlow<NavigationDestination>(InitialDestination)
    val navigationDestination = _navigationDestination.asStateFlow()

    private val arguments = mutableMapOf<DestinationId, DestinationArgument>()
    private val results = mutableMapOf<DestinationId, DestinationArgument>()

    fun navigateUp(args: DestinationArgument? = null) {
        _navigationDestination.value = BackDestination
    }

    fun navigateTo(destination: ForwardDestination, args: DestinationArgument? = null) {
        args?.let { arguments[destination.id] = it }
        _navigationDestination.value = destination
    }

    fun getArgument(destinationId: DestinationId) = arguments.remove(destinationId)
}
