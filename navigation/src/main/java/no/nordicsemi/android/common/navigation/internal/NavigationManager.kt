/*
 * Copyright (c) 2023, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.common.navigation.internal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import no.nordicsemi.android.common.navigation.*
import javax.inject.Inject

/**
 * A navigation manager that can be used to navigate to next destination, or back.
 *
 * @param context the application context.
 * @property savedStateHandle The [SavedStateHandle] that will be used to store the navigation
 * result.
 */
@ActivityRetainedScoped
class NavigationManager @Inject constructor(
    @ApplicationContext private val context: Context,
) : Navigator {
    /** The navigation events class. */
    sealed class Event {
        data class NavigateTo(
            val route: String,
            val args: Bundle?,
            val navOptions: NavOptions? = null,
            val counter: Int = Event.counter++
        ) : Event()

        data class NavigateUp(val result: Any?, val counter: Int = Event.counter++) : Event()

        companion object {
            private var counter = 0
        }
    }

    /** Savable results that can be stored in [SavedStateHandle]. */
    sealed class Result : Parcelable {
        @Parcelize
        object Initial : Result()
        @Parcelize
        object Cancelled : Result()
        @Parcelize
        data class Success<R>(val value: @RawValue R) : Result()
    }

    private val map = mutableMapOf<DestinationId<*, *>, MutableStateFlow<Boolean>>()
    private val _events = MutableStateFlow<Event?>(null)
    internal val events = _events.asStateFlow()
    internal var savedStateHandle: SavedStateHandle? = null
    internal var currentBackStackEntryFlow: Flow<NavBackStackEntry>? = null
        set(value) {
            // Cancel any previous job.
            scope.coroutineContext.cancelChildren()

            field = value
            value?.also { currentBackStackEntryFlow ->
                // For all existing hierarchy observers start collecting using the new flow.
                map.forEach { entry ->
                    currentBackStackEntryFlow.combine(entry)
                }
                // Also, start observing for current destination.
                scope.launch {
                    currentBackStackEntryFlow.collect { entry ->
                        //Log.d("Navigator", "Hierarchy: ${entry.destination.hierarchy.map { it.route }.reduce { acc, s -> "$acc | $s" }}")
                        entry.destination.route?.let { route ->
                            if (currentDestinationFlow.value?.name != route) {
                                currentDestinationFlow.value = createSimpleDestination(route)
                            }
                        }
                    }
                }
            }
        }
    private var currentDestinationFlow = MutableStateFlow<DestinationId<*, *>?>(null)

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun <R> resultFrom(from: DestinationId<*, R>): Flow<NavigationResult<R>> =
        @Suppress("UNCHECKED_CAST")
        savedStateHandle?.run {
            getStateFlow<Result>(from.name, Result.Initial)
                .onEach { this[from.name] = Result.Initial }
                .transform { result ->
                    when (result) {
                        // Ignore the initial value.
                        is Result.Initial -> {}
                        // Return success result.
                        is Result.Success<*> -> emit(NavigationResult.Success(result.value as R))
                        // Return null when cancelled.
                        is Result.Cancelled -> emit(NavigationResult.Cancelled())
                    }
                }
        } ?: throw IllegalStateException("SavedStateHandle is not set")

    override fun <A> navigateTo(to: DestinationId<A, *>, args: A, navOptions: NavOptions?) {
        val bundle = if (args is Unit) bundleOf() else bundleOf(to.name to args)
        _events.update { Event.NavigateTo(to.name, bundle, navOptions) }
    }

    override fun <R> navigateUpWithResult(from: DestinationId<*, R>, result: R) {
        _events.update { Event.NavigateUp(Result.Success(result)) }
    }

    override fun navigateUp() {
        _events.update { Event.NavigateUp(Result.Cancelled) }
    }

    override fun isInHierarchy(destination: DestinationId<*, *>): StateFlow<Boolean> =
        map.getOrPut(destination) {
            MutableStateFlow(false).apply {
                currentBackStackEntryFlow?.combine(this, destination)
            }
        }

    override fun currentDestination(): StateFlow<DestinationId<*, *>?> {
        return currentDestinationFlow
    }

    override fun open(link: Uri) {
        try {
            with(Intent(Intent.ACTION_VIEW, link)) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(this)
            }
        } catch (e: Exception) {
            Log.e("Navigator", "Failed to open link: $link", e)
        }
    }

    /**
     * After the navigation is completed, this method should be called to consume the event.
     * Otherwise, it will be emitted again. This covers a case, when the event was received, but
     * the consumer was destroyed before it could handle it.
     */
    fun consumeEvent(event: Event) {
        _events.compareAndSet(event, null)
    }

    private fun Flow<NavBackStackEntry>.combine(
        flow: MutableStateFlow<Boolean>,
        destination: DestinationId<*, *>
    ) {
        scope.launch {
            map { hierarchy -> destination in hierarchy }.collect { value -> flow.update { value } }
        }
    }

    private fun Flow<NavBackStackEntry>.combine(
        entry: Map.Entry<DestinationId<*, *>, MutableStateFlow<Boolean>>
    ) {
        combine(entry.value, entry.key)
    }

    private operator fun NavBackStackEntry.contains(dest: DestinationId<*, *>): Boolean =
        dest.name in destination.hierarchy.map { it.route }
}