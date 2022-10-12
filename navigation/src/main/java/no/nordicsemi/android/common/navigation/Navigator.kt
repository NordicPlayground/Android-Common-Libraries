package no.nordicsemi.android.common.navigation

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.SavedStateHandle

interface Navigator {
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
    fun navigate(hint: Any? = null, args: Bundle? = null)

    /**
     * Requests navigation using the given argument and suspends until the result is received.
     *
     * The destination target is determined either by the [Router] for the
     * local [NavigationDestinations], or the global [Router] set in
     * [NavigationView].
     *
     * The target destination must call [navigateUpWithResult] to return the result,
     * or [navigateUp] to cancel. If cancelled, null will be returned.
     *
     * @param hint An optional hint to determine the target destination.
     * @param args An optional argument to pass to the destination.
     */
    suspend fun <T> navigateForResult(hint: Any? = null, args: Bundle? = null): T?

    /**
     * Navigates up to previous destination, or finishes the Activity.
     */
    fun navigateUp()

    /**
     * Navigates up to previous destination passing the given result, or finishes the Activity.
     *
     * @param result The result, which will be passed to the previous destination.
     * The returned type will be saved in [SavedStateHandle], therefore it must be
     * savable to a [Bundle].
     */
    fun navigateUpWithResult(result: Any)

    /**
     * Opens the given link in a browser.
     */
    fun open(link: Uri)
}