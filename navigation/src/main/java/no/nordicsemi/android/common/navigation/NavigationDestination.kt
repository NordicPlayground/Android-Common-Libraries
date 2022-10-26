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

/**
 * A destination identifier.
 *
 * @param A The Argument type.
 * @param R The Result type.
 */
data class DestinationId<A, R>(internal val name: String) {
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
    val id: DestinationId<*, *>,
    val content: @Composable () -> Unit
) {

    operator fun plus(other: NavigationDestination): List<NavigationDestination> {
        return listOf(this, other)
    }

    operator fun plus(other: List<NavigationDestination>): List<NavigationDestination> {
        return listOf(this) + other
    }
}

/**
 * Helper function to create a [DestinationId] from a string.
 *
 * @param A The argument type of the destination.
 * @param R The return type of the destination.
 */
fun <A, R> createDestination(name: String): DestinationId<A, R> = DestinationId(name)

/**
 * Helper function to create a [DestinationId] from a string.
 *
 * The destination does not take any arguments and does not return any result.
 */
fun createSimpleDestination(name: String): DestinationId<Unit, Unit> = DestinationId(name)

/**
 * Helper method for creating a [NavigationDestination].
 */
fun defineDestination(
    id: DestinationId<*, *>,
    content: @Composable () -> Unit
): NavigationDestination = NavigationDestination(id, content)