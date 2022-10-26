@file:Suppress("unused")

package no.nordicsemi.android.common.navigation

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.RawValue

interface Navigator {

    /**
     * Creates a flow that will emit the results of the navigation from the given destination.
     *
     * @param from The origin destination to listen for results from.
     */
    fun <R> resultFrom(from: DestinationId<*, R>): Flow<NavigationResult<R>>

    /**
     * Requests navigation to the given destination. An optional parameter can be passed.
     *
     * @param to The destination to navigate to.
     * @param args An optional argument to pass to the destination. The argument will be saved
     * in [SavedStateHandle], therefore it must be savable to a [Bundle].
     */
    fun <A> navigateTo(to: DestinationId<A, *>, args: @RawValue A)

    /**
     * Navigates up to previous destination, or finishes the Activity.
     */
    fun navigateUp()

    /**
     * Navigates up to previous destination passing the given result, or finishes the Activity.
     *
     * @param from The destination from which navigating up.
     * @param result The result, which will be passed to the previous destination.
     * The returned object will be saved in [SavedStateHandle], therefore it must be
     * savable to a [Bundle].
     */
    fun <R> navigateUpWithResult(from: DestinationId<*, R>, result: @RawValue R)

    /**
     * Opens the given link in a browser.
     */
    fun open(link: Uri)
}

/**
 * Returns the argument for the current destination as Flow.
 *
 * @param destination The current destination.
 */
fun <A> SavedStateHandle.getStateFlow(destination: DestinationId<A, *>, initial: A?): StateFlow<A?> =
    getStateFlow(destination.name, initial)

/**
 * Returns the argument for the current destination.
 *
 * @param destination The current destination.
 */
fun <A> SavedStateHandle.getOrNull(destination: DestinationId<A?, *>): A? =
    get(destination.name)

/**
 * Returns the argument for the current destination.
 *
 * @param destination The current destination.
 */
fun <A> SavedStateHandle.get(destination: DestinationId<A & Any, *>): A =
    get(destination.name) ?: error("Destination '${destination.name}' requires a non-nullable argument")