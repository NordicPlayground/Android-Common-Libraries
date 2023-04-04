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

package no.nordicsemi.android.common.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import no.nordicsemi.android.common.logger.view.LoggerAppBarIcon
import no.nordicsemi.android.common.navigation.*
import no.nordicsemi.android.common.navigation.viewmodel.SimpleNavigationViewModel
import no.nordicsemi.android.common.test.main.MainDestinations
import no.nordicsemi.android.common.test.scanner.Scanner
import no.nordicsemi.android.common.test.simple.*
import no.nordicsemi.android.common.test.tab.*
import no.nordicsemi.android.common.theme.NordicActivity
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.*

data class Item(val title: String, val destinationId: DestinationId<Unit , *>, val icon: ImageVector)

val Tabs = createSimpleDestination("tabs")
val FirstTab = createSimpleDestination("first_tab")
val SecondTab = createSimpleDestination("second_tab")

@AndroidEntryPoint
class MainActivity : NordicActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDecorFitsSystemWindows(false)

        val menuItems = listOf(
            Item("Main", Tabs, Icons.Filled.Verified),
            Item("Settings", Settings, Icons.Filled.Settings),
        )

        val advancedMenuItems = listOf(
            Item("Advanced Settings", Advanced, Icons.Filled.ImageSearch),
        )

        val tabs = listOf(
            Item("Main", FirstTab, Icons.Filled.Verified),
            Item("Inner Navigation", SecondTab, Icons.Filled.Navigation),
            Item("Simple", ThirdTab, Icons.Filled.Album),
        )

        setContent {
            NordicTheme {
                val navigator: SimpleNavigationViewModel = hiltViewModel()

                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            modifier = Modifier
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState()),
                        ) {
                            NordicLogo(
                                modifier = Modifier
                                    .padding(NavigationDrawerTitleDefaults.ItemPadding)
                                    .padding(vertical = 16.dp)
                            )

                            NavigationDrawerTitle(
                                title = "Menu",
                                modifier = Modifier.padding(NavigationDrawerTitleDefaults.ItemPadding)
                            )

                            NavigationDrawerItems(
                                items = menuItems,
                                navigator = navigator,
                                onDismiss = { scope.launch { drawerState.close() } },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )

                            Divider(
                                modifier = Modifier.padding(NavigationDrawerDividerDefaults.ItemPadding)
                            )

                            NavigationDrawerTitle(
                                title = "Advanced",
                                modifier = Modifier.padding(NavigationDrawerTitleDefaults.ItemPadding)
                            )

                            NavigationDrawerItems(
                                items = advancedMenuItems,
                                navigator = navigator,
                                onDismiss = { scope.launch { drawerState.close() } },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )

                            NavigationDrawerItem(
                                icon = { Icon(Icons.Filled.BrokenImage, contentDescription = null) },
                                label = { Text("This does nothing") },
                                selected = false,
                                onClick = { },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    }
                ) {
                    val currentDestination by navigator.currentDestination().collectAsStateWithLifecycle()
                    Scaffold(
                        topBar = {
                            NordicAppBar(
                                text = stringResource(id = R.string.title_main),
                                showBackButton = listOf(Hello, HelloDialog, Scanner).contains(currentDestination),
                                onNavigationButtonClick = { navigator.navigateUp() },
                                onHamburgerButtonClick = {
                                    scope.launch { drawerState.open() }
                                },
                                actions = {
                                    val context = LocalContext.current
                                    LoggerAppBarIcon(
                                        onClick = {
                                            Toast.makeText(
                                                context,
                                                "Logger clicked",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                },
                            )
                        },
                        bottomBar = {
                            // Show the Navigation Bar only in the Tabs destination.
                            if (navigator.isInHierarchy(Tabs).collectAsStateWithLifecycle().value) {
                                NavigationBar {
                                    tabs.forEach { tab ->
                                        val selected by navigator.isInHierarchy(tab.destinationId).collectAsStateWithLifecycle()
                                        NavigationBarItem(
                                            icon = { Icon(tab.icon, contentDescription = null) },
                                            label = { Text(tab.title) },
                                            selected = selected,
                                            onClick = {
                                                // Checking if the tab is not selected here
                                                // is a workaround for an issue with how the navigation
                                                // restores the previous stack when back button was used.
                                                // See: https://issuetracker.google.com/issues/258237571
                                                if (!selected) {
                                                    navigator.navigateTo(tab.destinationId) {
                                                        popUpToStartDestination {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    ) { padding ->
                        NavigationView(
                            destinations = listOf(
                                Tabs with ((FirstTab with MainDestinations) + (SecondTab with SecondDestinations) + ThirdDestination),
                                SettingsDestination,
                                AdvancedDestination,
                            ),
                            modifier = Modifier.padding(padding),
                            backHandler = {
                                // The back handler is called when user click the back button,
                                // either the physical or any action that calls navigator.navigateUp().
                                // The handler can handle it and return true (handled) or ignore,
                                // to continue with a default behavior.
                                drawerState.isOpen.also { if (it) scope.launch { drawerState.close() } }
                            }
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavigationDrawerItems(
    items: List<Item>,
    navigator: Navigator,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    items.forEach { item ->
        val selected by navigator.isInHierarchy(item.destinationId).collectAsStateWithLifecycle()
        NavigationDrawerItem(
            icon = { Icon(item.icon, contentDescription = null) },
            label = { Text(item.title) },
            selected = selected,
            onClick = {
                onDismiss()
                // Checking if the tab is not selected here
                // is a workaround for an issue with how the navigation
                // restores the previous stack when back button was used.
                // See: https://issuetracker.google.com/issues/258237571
                if (!selected) {
                    navigator.navigateTo(item.destinationId) {
                        popUpToStartDestination {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            modifier = modifier,
        )
    }
}