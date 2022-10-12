package no.nordicsemi.android.common.navigation.internal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import no.nordicsemi.android.common.navigation.NavigationDestination
import javax.inject.Inject

@HiltViewModel
internal class NavigationViewModel @Inject constructor(
    private val navigationManager: NavigationManager,
): ViewModel() {

    fun use(executor: NavigationExecutor) = this
        .apply{ navigationManager.executor = executor }

    fun at(destination: NavigationDestination) = this
        .apply { navigationManager.currentDestinationId = destination.id }

    fun use(savedStateHandle: SavedStateHandle) = this
        .apply { navigationManager.savedStateHandle = savedStateHandle }

    override fun onCleared() {
        super.onCleared()
        navigationManager.executor = null
    }
}