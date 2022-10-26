package no.nordicsemi.android.common.navigation.internal

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class NavigationViewModel @Inject constructor(
    private val navigationManager: NavigationManager,
): ViewModel(), NavigationExecutor {
    /** The navigation events class. */
    sealed class Event {
        data class NavigateTo(val route: String, val args: Bundle?) : Event()
        data class NavigateUp(val result: Any?) : Event()
    }

    private val _events = MutableStateFlow<Event?>(null)
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
    fun use(savedStateHandle: SavedStateHandle) = apply {
        navigationManager.savedStateHandle = savedStateHandle
    }

    /**
     * After the navigation is completed, this method should be called to consume the event.
     * Otherwise, it will be emitted again. This covers a case, when the event was received, but
     * the consumer was destroyed before it could handle it.
     */
    fun consumeEvent() {
        _events.update { null }
    }

    override fun <A> navigate(target: NavigationTarget<A>) {
        _events.update { Event.NavigateTo(target.destination.name, target.toBundle()) }
    }

    override fun navigateUpWithResult(result: NavigationResultState) {
        _events.update { Event.NavigateUp(result) }
    }

    override fun onCleared() {
        super.onCleared()
        navigationManager.executor = null
    }
}