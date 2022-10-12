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

import androidx.compose.runtime.Composable
import no.nordicsemi.android.common.navigation.internal.combine

/**
 * A callback used to determine the next destination.
 *
 * Based on the current destination (from), a hint, and the optional arguments, the callback should
 * return the target destination, or null, if that router does not support the given destination.
 */
typealias Router = (from: DestinationId, hint: Any?) -> DestinationId?

/**
 * A destination identifier.
 */
data class DestinationId(internal val name: String) {
    override fun toString(): String = name
}

/**
 * Definition of a destination.
 *
 * This class binds a destination identifier with a composable function that will be used to
 * render the content.
 *
 * @property id The destination identifier.
 * @property content The composable function that will be used to render the content.
 */
data class NavigationDestination(
    val id: DestinationId,
    val content: @Composable (navigator: Navigator, result: ResultHandle) -> Unit
) {
    constructor(
        id: String,
        content: @Composable (navigator: Navigator, result: ResultHandle) -> Unit
    ): this(DestinationId(id), content)

    operator fun plus(other: NavigationDestination): NavigationDestinations {
        return listOf(this, other).asDestinations()
    }
}

/**
 * A collection of destinations.
 *
 * Navigation between the destinations may be controlled by local or global navigation controllers.
 * Local navigation controller, defined here, can navigate between destinations within the
 * collection, where some of the destinations may not be exposed outside of the component.
 *
 * The global navigation controller, defined in [NavigationView], can navigate between destinations
 * from different components.
 *
 * @property values List of destinations within a component.
 * @property router An optional router that can route between destinations within this component.
 */
class NavigationDestinations(
    val values: List<NavigationDestination>,
    val router: Router = { _, _ -> null },
) {
    constructor(
        destination: NavigationDestination,
        router: Router = { _, _ -> null },
    ) : this(listOf(destination), router)

    operator fun plus(other: NavigationDestinations): NavigationDestinations {
        val combinedRouter = router.combine(other.router)
        return NavigationDestinations(values + other.values, combinedRouter)
    }

    operator fun plus(other: Router): NavigationDestinations {
        val combinedRouter = router.combine(other)
        return NavigationDestinations(values, combinedRouter)
    }
}

/**
 * Helper function to create a [DestinationId] from a string.
 */
fun createDestination(name: String): DestinationId = DestinationId(name)

/**
 * Helper method for creating a [NavigationDestination].
 */
fun defineDestination(
    id: DestinationId,
    content: @Composable (navigator: Navigator) -> Unit
): NavigationDestination = NavigationDestination(id) { navigator, _ -> content(navigator) }

/**
 * Helper method for creating a [NavigationDestination] that will
 * be able to subscribe for results from destinations it will navigate to.
 */
fun defineDestination(
    id: DestinationId,
    content: @Composable (navigator: Navigator, result: ResultHandle) -> Unit,
): NavigationDestination = NavigationDestination(id, content)

/**
 * Helper method for creating a [NavigationDestinations]
 * with local router.
 */
fun List<NavigationDestination>.asDestinationsWithRouter(router: Router) =
    NavigationDestinations(this, router)

/**
 * Helper method for creating a [NavigationDestinations].
 *
 * This destination can be routed using global router specified
 * in the [NavigationView].
 *
 * @see asDestinationsWithRouter
 */
fun List<NavigationDestination>.asDestinations() =
    NavigationDestinations(this) { _, _ -> null }

/**
 * Helper method for creating a [NavigationDestinations]
 * from a single destination.
 *
 * This destination can be routed using global router specified
 * in the [NavigationView].
 *
 * @see asDestinationsWithRouter
 */
fun NavigationDestination.asDestinations() =
    NavigationDestinations(this) { _, _ -> null }