package no.nordicsemi.android.common.navigation.internal

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

internal sealed class NavigationEvent
internal data class NavigateTo(val route: String, val args: Bundle?) : NavigationEvent()
internal data class NavigateUp(val result: Any?) : NavigationEvent()

@HiltViewModel
internal class NavigationViewModel @Inject constructor(
    private val navigationManager: NavigationManager,
): ViewModel() {
    private val _events = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    init {
        // TODO Remove the NavigationExecutor, use NavigationViewModel instead?
        navigationManager.executor = NavigationExecutor(
            onNavigateTo = { route, args -> _events.tryEmit(NavigateTo(route, args)) },
            onNavigateUp = { result -> _events.tryEmit(NavigateUp(result)) },
        )
    }

    fun use(savedStateHandle: SavedStateHandle) = this
        .apply { navigationManager.savedStateHandle = savedStateHandle }

    fun navigateUp() = navigationManager.navigateUp()

    override fun onCleared() {
        super.onCleared()
        navigationManager.executor = null
    }
}