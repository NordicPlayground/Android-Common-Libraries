package no.nordicsemi.android.common.navigation.internal

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import no.nordicsemi.android.common.navigation.DestinationId
import no.nordicsemi.android.common.navigation.Router
import no.nordicsemi.android.common.navigation.Navigator

/**
 * The navigation performer calculates the target destination by using the navigation controller
 * and then performs the navigation.
 *
 * @property router The [Router] used to calculate the target destination.
 * @property onNavigateTo The callback invoked when the navigation is performed.
 * @property onStoreResult The callback invoked to store the result of the destination.
 * @property onNavigateUp The callback invoked when the navigation up is performed.
 */
internal class NavigationPerformer(
    private val router: Router,
    private val onNavigateTo: (String, Bundle?) -> Unit,
    private val onStoreResult: (Any) -> Unit,
    private val onNavigateUp: () -> Unit,
) {
    /**
     * Returns a [Navigator] that will allow navigating from the current destination.
     */
    internal fun from(destinationId: DestinationId): Navigator = Navigator(this, destinationId)

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
        } ?: throw IllegalStateException("No destination found for $from with using $hint")
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
        onNavigateUp()
    }

    /**
     * Navigate up to the previous destination passing the given result.
     *
     * @param result The result, which will be passed to the previous destination.
     * The returned type will be saved in [SavedStateHandle], therefore it must be
     * savable to a [Bundle].
     */
    internal fun navigateUpWithResult(result: Any) {
        onStoreResult(result)
        onNavigateUp()
    }
}