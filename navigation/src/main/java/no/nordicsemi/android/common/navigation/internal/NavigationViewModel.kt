package no.nordicsemi.android.common.navigation.internal

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import no.nordicsemi.android.common.navigation.DestinationId
import javax.inject.Inject

internal sealed class NavigationEvent
internal data class NavigateTo(val route: String, val args: Bundle?) : NavigationEvent()
internal data class NavigateUp(val result: Any?) : NavigationEvent()

@HiltViewModel
internal class NavigationViewModel @Inject constructor(
    private val navigationManager: NavigationManager,
): ViewModel(), NavigationExecutor {
    private val _events = MutableStateFlow<NavigationEvent?>(null)
    val events = _events.asStateFlow()

    init {
        navigationManager.executor = this
    }

    /**
     * The given [SavedStateHandle] will be used to store the navigation result.
     *
     * This has to be the instance given in navigation composable, as each ViewModel may received
     * a different instance of [SavedStateHandle] using Hilt injections.
     */
    fun use(savedStateHandle: SavedStateHandle) = this
        .apply { navigationManager.savedStateHandle = savedStateHandle }

    /**
     * After the navigation is completed, this method should be called to consume the event.
     * Otherwise, it will be emitted again. This covers a case, when the event was received, but
     * the consumer was destroyed before it could handle it.
     */
    fun consumeEvent() {
        _events.update { null }
    }

    override fun navigate(to: DestinationId<*>, args: Bundle?) {
        _events.update { NavigateTo(to.name, args) }
    }

    override fun navigateUpWithResult(result: NavigationResult) {
        _events.update { NavigateUp(result) }
    }

    override fun onCleared() {
        super.onCleared()
        navigationManager.executor = null
    }
}