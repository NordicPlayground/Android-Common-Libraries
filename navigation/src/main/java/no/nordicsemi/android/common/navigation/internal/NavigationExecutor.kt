package no.nordicsemi.android.common.navigation.internal

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import no.nordicsemi.android.common.navigation.DestinationId
import no.nordicsemi.android.common.navigation.Router

/**
 * This object calculates the target destination by using destination routers
 * and then requests the navigation.
 *
 * @property router The [Router] used to calculate the target destination.
 * @property onNavigateTo Navigates to the given route, passing optional argument.
 * @property onNavigateUp Navigates up, passing an optional result.
 */
internal class NavigationExecutor(
    private val router: Router,
    private val onNavigateTo: (route: String, Bundle?) -> Unit,
    private val onNavigateUp: (Any?) -> Unit,
) {
    /**
     * Navigate to the given destination passing an optional argument.
     *
     * @param from The current destination.
     * @param args An optional argument to pass to the destination.
     */
    internal fun navigate(from: DestinationId, hint: Any?, args: Bundle?) {
        // Find next destination from "from" given the argument.
        router(from, hint)?.let {
            // If found, navigate to it.
            navigateTo(it, args)
        } ?: throw IllegalStateException("No destination found for $from with using $hint as hint")
    }

    /**
     * Navigates to the given destination passing an optional argument.
     */
    private fun navigateTo(destinationId: DestinationId, arg: Bundle?) {
        onNavigateTo(destinationId.name, arg)
    }

    /**
     * Navigate up to the previous destination.
     */
    internal fun navigateUp() {
        onNavigateUp(Cancelled)
    }

    /**
     * Navigate up to the previous destination passing the given result.
     *
     * @param result The result, which will be passed to the previous destination.
     * The returned type will be saved in [SavedStateHandle], therefore it must be
     * savable to a [Bundle].
     */
    internal fun navigateUpWithResult(result: Any) {
        onNavigateUp(result)
    }
}