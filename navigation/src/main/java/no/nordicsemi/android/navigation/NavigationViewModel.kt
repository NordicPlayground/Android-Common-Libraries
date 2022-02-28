package no.nordicsemi.android.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val navigationManager: NavigationManager,
) : ViewModel() {

    var navigationWrapper: NavigationWrapper? = null

    init {
        navigationManager.navigationDestination.onEach { destination ->
            navigationWrapper?.let {
                it.consumeEvent(destination)
                consumeLastEvent()
            }
        }.launchIn(viewModelScope)
    }

    fun navigateUp() {
        navigationManager.navigateUp()
    }

    private fun consumeLastEvent() {
        navigationManager.consumeLastEvent()
    }
}
