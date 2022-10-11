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

package no.nordicsemi.android.common.navigation

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.nordicsemi.android.common.navigation.internal.NavigationPerformer
import no.nordicsemi.android.common.navigation.internal.combine
import no.nordicsemi.android.common.navigation.internal.navigate

/**
 * A navigation view allows navigating between different destinations.
 *
 * Each destination may pass an argument to the next one. The argument is a [Bundle].
 *
 * @param destinations The list of possible destinations.
 * @param router The navigation controller used to calculate target destinations.
 */
@Composable
fun NavigationView(
    destinations: NavigationDestinations,
    router: Router = { _, _ -> null }
) {
    val navHostController = rememberNavController()

    val activity = LocalContext.current as Activity

    // Create a combined navigator that will navigate between destinations.
    val combinedRouter = router.combine(destinations.router)
    val navigator = NavigationPerformer(combinedRouter,
        onNavigateTo = { destination, argument ->
            // Navigate to the next destination, passing the argument.
            navHostController.navigate(destination, argument)
        },
        onStoreResult = { result ->
            // Save the result in the previous back stack entry.
            navHostController.currentBackStackEntry?.destination?.route?.let { route ->
                // The key is the current destination id.
                navHostController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(route, result)
            }
        },
        onNavigateUp = {
            // Navigate up, or finish the Activity if at root.
            if (!navHostController.navigateUp()) {
                activity.finish()
            }
        }
    )

    BackHandler { navigator.navigateUp() }

    NavHost(
        navController = navHostController,
        startDestination = destinations.values.first().id.name,
    ) {
        destinations.values.forEach { destination ->
            composable(
                route = destination.id.name,
            ) {
                DestinationScreen(
                    destination = destination,
                    navigator = navigator.from(destination.id),
                    savedStateHandle = it.savedStateHandle,
                )
                // The above does the same as:
                //
                // destination.content(navigator.from(destination.id))
                //
                // but looks like proper composable.
            }
        }
    }
}

@Composable
private fun DestinationScreen(
    destination: NavigationDestination,
    navigator: Navigator,
    savedStateHandle: SavedStateHandle,
) = with(destination) { content(navigator, ResultHandle(savedStateHandle)) }
