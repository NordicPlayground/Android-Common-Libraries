package no.nordicsemi.android.common.navigation.internal

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle

/**
 * A navigation executor that can be used to navigate to next destination, or back.
 */
internal interface NavigationExecutor {
    /**
     * Navigates to the given destination passing an optional argument.
     *
     * @param target The target target with an optional parameter.
     */
    fun navigate(target: NavigationTarget)

    /**
     * Navigate up to the previous destination passing the given result.
     *
     * @param result The result, which will be passed to the previous destination.
     * The returned type will be saved in [SavedStateHandle], therefore it must be
     * savable to a [Bundle].
     */
    fun navigateUpWithResult(result: NavigationResult)
}