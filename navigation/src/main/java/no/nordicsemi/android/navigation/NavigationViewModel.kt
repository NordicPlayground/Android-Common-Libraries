package no.nordicsemi.android.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val navigationManager: NavigationManager,
) : ViewModel() {

    val destination = navigationManager.navigationDestination

    fun navigateUp() {
        navigationManager.navigateUp()
    }
}
