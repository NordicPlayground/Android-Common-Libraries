/*
 * Copyright (c) 2022, Nordic Semiconductor
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
     * Requests navigation to the given destination. An required parameter must be passed.
     *
     * @param to The destination to navigate to.
     * @param args An optional argument to pass to the destination. The argument will be saved
     * in [SavedStateHandle], therefore it must be savable to a [Bundle].
     */
    fun <A> navigateTo(to: DestinationId<A, *>, args: @RawValue A)

    /**
     * Requests navigation to the given destination which takes no input parameter.
     *
     * @param to The destination to navigate to.
     */
    fun navigateTo(to: DestinationId<Unit, *>) = navigateTo(to, Unit)

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

/**
 * Returns the argument for the current destination.
 *
 * @param destination The current destination.
 */
fun SavedStateHandle.get(destination: DestinationId<Unit, *>): Nothing =
    error("Destination '${destination.name}' does not have an argument")