package no.nordicsemi.android.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val navigationManager: NavigationManager,
) : ViewModel() {

    val destination = navigationManager.navigationDestination.map {
        it.also {
            delay(50) //delay to make two consecutive emissions working
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, NavigationDestinationComposeHelper(InitialDestination))

    fun navigateUp() {
        navigationManager.navigateUp()
    }

    fun consumeLastEvent() {
        navigationManager.consumeLastEvent()
    }
}
