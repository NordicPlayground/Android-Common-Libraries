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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.nordicsemi.android.common.navigation.internal.Cancelled
import no.nordicsemi.android.common.navigation.internal.NavigationExecutor
import no.nordicsemi.android.common.navigation.internal.NavigationViewModel
import no.nordicsemi.android.common.navigation.internal.navigate

/**
 * A navigation view allows navigating between different destinations.
 *
 * Each destination may pass an argument to the next one. The argument is a [Bundle].
 *
 * @param destinations The list of possible destinations.
 */
@Composable
fun NavigationView(
    destinations: NavigationDestinations,
) {
    val navHostController = rememberNavController()

    val activity = LocalContext.current as Activity

    val executor = NavigationExecutor(
        onNavigateTo = { route, argument ->
            // Navigate to the next destination, passing the argument.
            navHostController.navigate(route, argument)
        },
        onNavigateUp = { result ->
            // Navigate up to the previous destination, passing the result.
            navHostController.currentBackStackEntry?.destination?.route?.let { route ->
                navHostController.previousBackStackEntry?.savedStateHandle?.set(route, result)
            }

            // Navigate up, or finish the Activity if at root.
            if (!navHostController.navigateUp()) {
                activity.finish()
            }
        }
    )

    val navigation = hiltViewModel<NavigationViewModel>().apply { use(executor) }

    BackHandler { executor.navigateUpWithResult(Cancelled) }

    NavHost(
        navController = navHostController,
        startDestination = destinations.values.first().id.name,
    ) {
        destinations.values.forEach { destination ->
            composable(
                route = destination.id.name,
            ) {
                navigation.use(it.savedStateHandle)
                destination.content()
            }
        }
    }
}
