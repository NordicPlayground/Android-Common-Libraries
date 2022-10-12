package no.nordicsemi.android.common.navigation

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
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
    fun navigate(hint: Any? = null, args: Bundle? = null) {
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

    /**
     * Navigates up to previous destination passing the given result, or finishes the Activity.
     *
     * @param result The result, which will be passed to the previous destination.
     * The returned type will be saved in [SavedStateHandle], therefore it must be
     * savable to a [Bundle].
     */
    fun navigateUpWithResult(result: Any) {
        performer.navigateUpWithResult(result)
    }
}