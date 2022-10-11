package no.nordicsemi.android.common.navigation

import androidx.lifecycle.SavedStateHandle

/**
 * An object which allows subscribing to navigation results.
 *
 * When a destination navigates to a new destination for a result,
 * it must subscribe for the results using this object.
 *
 * @property savedStateHandle The [SavedStateHandle] used to collect results.
 *           This is the saved state handle of the current destination.
 */
class ResultHandle internal constructor(private val savedStateHandle: SavedStateHandle) {

    /**
     * Returns the results from the given destination as State Flow.
     *
     * @param destination The destination identifier of the target destination.
     * @param initialValue Initial value returned by the State Flow.
     */
    fun <T> resultFrom(destination: DestinationId, initialValue: T) =
        savedStateHandle.getStateFlow(destination.name, initialValue)

}