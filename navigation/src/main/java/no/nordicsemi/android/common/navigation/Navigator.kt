package no.nordicsemi.android.common.navigation

import android.os.Bundle
import no.nordicsemi.android.common.navigation.internal.NavigationPerformer

/**
 * A navigator that can be used to navigate to next destination, or back.
 *
 * @property performer The [NavigationPerformer] that will perform the navigation.
 * @property currentDestinationId The identifier of the current destination.
 */
class Navigator internal constructor (
    private val performer: NavigationPerformer,
    private val currentDestinationId: DestinationId,
) {
    /**
     * Requests navigation using the given argument.
     *
     * The destination target is determined either by the [Router] for the
     * local [NavigationDestinations], or the global [Router] set in
     * [NavigationView].
     *
     * @param hint An optional hint to determine the target destination.
     * @param args An optional argument to pass to the destination.
     */
    fun navigate(hint: Any?, args: Bundle? = null) {
        performer.navigate(
            from = currentDestinationId,
            hint = hint,
            args = args,
        )
    }

    /**
     * Navigates up to previous destination, or finishes the Activity.
     */
    fun navigateUp() {
        performer.navigateUp()
    }
}