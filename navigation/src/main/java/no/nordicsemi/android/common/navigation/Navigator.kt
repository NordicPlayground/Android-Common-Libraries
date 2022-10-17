package no.nordicsemi.android.common.navigation

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow

interface Navigator {

    /**
     * Creates a flow that will emit the results of the navigation from the given destination.
     *
     * Null is emitted when the navigation was cancelled.
     *
     * @param from The origin destination to listen for results from.
     */
    fun <T> resultFrom(from: DestinationId): Flow<T?>

    /**
     * Requests navigation to the given destination. An optional parameter can be passed.
     *
     * @param to The destination to navigate to.
     * @param args An optional argument to pass to the destination.
     */
    fun navigateTo(to: DestinationId, args: Bundle? = null)

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