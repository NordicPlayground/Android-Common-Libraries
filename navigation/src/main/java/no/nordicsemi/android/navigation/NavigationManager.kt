package no.nordicsemi.android.navigation

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {

    private val _navigationDestination = MutableStateFlow(NavigationDestinationComposeHelper(InitialDestination))
    val navigationDestination = _navigationDestination.asStateFlow()

    private val arguments = mutableMapOf<DestinationId, DestinationArgument>()
    private val results = mutableMapOf<DestinationId, DestinationResult>()

    val recentArgument = MutableSharedFlow<DestinationArgument>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val recentResult = MutableSharedFlow<DestinationResult>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun consumeLastEvent() {
        _navigationDestination.value = _navigationDestination.value.copy(isConsumed = true)
    }

    fun navigateUp() {
        postDestination(BackDestination)
    }

    fun navigateUp(destinationId: DestinationId, args: DestinationResult) {
        results[destinationId] = args
        recentResult.tryEmit(args)
        postDestination(BackDestination)
    }

    fun navigateTo(destination: ForwardDestination, args: DestinationArgument? = null) {
        args?.let {
            arguments[destination.id] = it
            recentArgument.tryEmit(it)
        }
        postDestination(destination)
    }

    private fun postDestination(destination: NavigationDestination) {
        _navigationDestination.value = NavigationDestinationComposeHelper(destination)
    }

    fun getImmediateArgument(destinationId: DestinationId) = arguments[destinationId]
}
