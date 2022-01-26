package no.nordicsemi.android.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val navigationManager: NavigationManager,
) : ViewModel() {

    var navController: NavHostController? = null
    init {
        navigationManager.navigationDestination.onEach { destination ->
            navController?.let { navController ->
                if (destination.isConsumed) {
                    return@let
                }
                when (val dest = destination.destination) {
                    BackDestination -> navController.navigateUp()
                    is ForwardDestination -> navController.navigate(dest.id.name)
                    InitialDestination -> doNothing() //Needed because collectAsState() requires initial state.
                }
                consumeLastEvent()
            }
        }.launchIn(viewModelScope)
    }

    fun navigateUp() {
        navigationManager.navigateUp()
    }

    fun consumeLastEvent() {
        navigationManager.consumeLastEvent()
    }
}
