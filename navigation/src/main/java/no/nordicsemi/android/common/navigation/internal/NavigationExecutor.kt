package no.nordicsemi.android.common.navigation.internal

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import no.nordicsemi.android.common.navigation.DestinationId

/**
 * This object calculates the target destination by using destination routers
 * and then requests the navigation.
 *
 * @property onNavigateTo Navigates to the given route, passing optional argument.
 * @property onNavigateUp Navigates up, passing an optional result.
 */
internal class NavigationExecutor(
    private val onNavigateTo: (route: String, Bundle?) -> Unit,
    private val onNavigateUp: (Any?) -> Unit,
) {
    /**
     * Navigates to the given destination passing an optional argument.
     *
     * @param to The target destination.
     * @param args An optional argument to pass to the destination.
     */
    internal fun navigate(to: DestinationId, args: Bundle?) {
        onNavigateTo(to.name, args)
    }

    /**
     * Navigate up to the previous destination passing the given result.
     *
     * @param result The result, which will be passed to the previous destination.
     * The returned type will be saved in [SavedStateHandle], therefore it must be
     * savable to a [Bundle].
     */
    internal fun navigateUpWithResult(result: NavigationResult) {
        onNavigateUp(result)
    }
}