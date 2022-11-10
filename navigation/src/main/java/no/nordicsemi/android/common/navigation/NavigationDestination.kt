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
import androidx.compose.ui.window.DialogProperties

/**
 * A navigation view allows navigating between different destinations.
 * @property id The destination id.
 */
sealed class NavigationDestination(
    val id: DestinationId<*, *>,
) {
    operator fun plus(other: NavigationDestination): List<NavigationDestination> {
        return listOf(this, other)
    }

    operator fun plus(other: List<NavigationDestination>): List<NavigationDestination> {
        return listOf(this) + other
    }
}

/**
 * Definition of a composable destination.
 *
 * This class binds a destination identifier with a composable function that will be used to
 * render the content.
 *
 * @property id The destination identifier.
 * @property content The composable function that will be used to render the content.
 */
internal class ComposableDestination(
    id: DestinationId<*, *>,
    val content: @Composable () -> Unit,
): NavigationDestination(id)

/**
 * Definition of an inner navigation.
 */
internal class InnerNavigationDestination(
    id: DestinationId<*, *>,
    val destinations: List<NavigationDestination>,
): NavigationDestination(id)

/**
 * Definition of a dialog destination.
 *
 * This class binds a destination identifier with a composable function that will be used to
 * render the content.
 *
 * @property id The destination identifier.
 * @property content The composable function that will be used to render the content.
 */
internal class DialogDestination(
    id: DestinationId<*, *>,
    val dialogProperties: DialogProperties = DialogProperties(),
    val content: @Composable () -> Unit,
): NavigationDestination(id)

/**
 * Helper method for creating a composable [NavigationDestination].
 */
fun defineDestination(
    id: DestinationId<*, *>,
    content: @Composable () -> Unit
): NavigationDestination = ComposableDestination(id, content)

/**
 * Helper method for creating inner navigation.
 */
fun defineDestinationWithInnerNavigation(
    id: DestinationId<*, *>,
    destinations: List<NavigationDestination>
): NavigationDestination = InnerNavigationDestination(id, destinations)

/**
 * Helper method for creating inner navigation.
 */
infix fun DestinationId<*, *>.with(destinations: List<NavigationDestination>): NavigationDestination {
    return defineDestinationWithInnerNavigation(this, destinations)
}

/**
 * Helper method for creating a dialog [NavigationDestination].
 */
fun defineDialogDestination(
    id: DestinationId<*, *>,
    dialogProperties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
): NavigationDestination = DialogDestination(id, dialogProperties, content)