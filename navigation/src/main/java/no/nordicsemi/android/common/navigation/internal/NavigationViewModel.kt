package no.nordicsemi.android.common.navigation.internal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class NavigationViewModel @Inject constructor(
    private val navigationManager: NavigationManager,
): ViewModel() {

    fun use(executor: NavigationExecutor) = this
        .apply{ navigationManager.executor = executor }

    fun use(savedStateHandle: SavedStateHandle) = this
        .apply { navigationManager.savedStateHandle = savedStateHandle }

    override fun onCleared() {
        super.onCleared()
        navigationManager.executor = null
    }
}