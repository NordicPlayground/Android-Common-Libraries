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

package no.nordicsemi.android.common.navigation

import android.app.Activity
import android.os.Bundle
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import no.nordicsemi.android.common.navigation.internal.NavigationManager.Event
import no.nordicsemi.android.common.navigation.internal.NavigationViewModel
import no.nordicsemi.android.common.navigation.internal.START_DESTINATION
import no.nordicsemi.android.common.navigation.internal.navigate

/**
 * A navigation view allows navigating between different destinations.
 *
 * Each destination may pass an argument to the next one. The argument is a [Bundle].
 *
 * @param destinations The list of possible destinations.
 * @param modifier The modifier to be applied to the layout.
 * @param enterTransition callback to define enter transitions for destination in this host
 * @param exitTransition callback to define exit transitions for destination in this host
 * @param popEnterTransition callback to define popEnter transitions for destination in this host
 * @param popExitTransition callback to define popExit transitions for destination in this host
 * @param sizeTransform callback to define the size transform for destinations in this host
 */
@Composable
fun NavigationView(
    destinations: List<NavigationDestination>,
    modifier: Modifier = Modifier,
    enterTransition:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
        {
            fadeIn(animationSpec = tween(700))
        },
    exitTransition:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
        {
            fadeOut(animationSpec = tween(700))
        },
    popEnterTransition:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
        enterTransition,
    popExitTransition:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
        exitTransition,
    sizeTransform:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)? =
        null,
) {
    val navHostController = rememberNavController()

    val navigation = hiltViewModel<NavigationViewModel>()
    navigation.use(navHostController.currentBackStackEntryFlow)

    // Handle navigation events.
    val event by navigation.events.collectAsStateWithLifecycle()

    event?.let { e ->
        when (e) {
            is Event.NavigateTo -> navHostController.navigate(e.route, e.args) {
                e.navOptions?.let { navOptions ->
                    when (val route = navOptions.popUpToRoute) {
                        START_DESTINATION -> popUpTo(navHostController.graph.findStartDestination().id) {
                            inclusive = navOptions.isPopUpToInclusive()
                            saveState = navOptions.shouldPopUpToSaveState()
                        }
                        is String -> popUpTo(route) {
                            inclusive = navOptions.isPopUpToInclusive()
                            saveState = navOptions.shouldPopUpToSaveState()
                        }
                        else -> {
                            // Do nothing.
                        }
                    }
                    launchSingleTop = navOptions.shouldLaunchSingleTop()
                    restoreState = navOptions.shouldRestoreState()
                }
            }
            is Event.NavigateUp -> {
                val activity = LocalContext.current as Activity
                // Navigate up to the previous destination, passing the result.
                navHostController.currentBackStackEntry?.destination?.route?.let { route ->
                    navHostController.previousBackStackEntry?.savedStateHandle?.set(route, e.result)
                }

                // Navigate up, or finish the Activity if at root.
                if (!navHostController.navigateUp()) {
                    activity.finish()
                }
            }
        }
        navigation.consumeEvent(e)
    }

    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = destinations.first().id.name,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        sizeTransform = sizeTransform,
    ) {
        create(destinations, navigation)
    }
}

private fun NavGraphBuilder.create(
    destinations: List<NavigationDestination>,
    navigation: NavigationViewModel,
) {
    destinations.forEach { destination ->
        when (destination) {
            is ComposableDestination -> {
                composable(
                    route = destination.id.name,
                ) {
                    navigation.use(it.savedStateHandle)
                    destination.content()
                }
            }
            is InnerNavigationDestination -> {
                navigation(
                    startDestination = destination.destinations.first().id.name,
                    route = destination.id.name
                ) {
                    create(destination.destinations, navigation)
                }
            }
            is DialogDestination -> {
                dialog(
                    route = destination.id.name,
                    dialogProperties = destination.dialogProperties,
                ) {
                    navigation.use(it.savedStateHandle)
                    destination.content()
                }
            }
        }
    }
}