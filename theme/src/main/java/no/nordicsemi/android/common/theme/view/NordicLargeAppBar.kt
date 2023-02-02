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

@file:OptIn(ExperimentalMaterial3Api::class)

package no.nordicsemi.android.common.theme.view

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import no.nordicsemi.android.common.theme.R

@Composable
fun NordicLargeAppBar(
    text: String,
    onNavigationButtonClick: (() -> Unit)? = null,
    onHamburgerButtonClick: (() -> Unit)? = null,
    showBackButton: Boolean = onNavigationButtonClick != null,
    showHamburgerButton: Boolean = onHamburgerButtonClick != null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    LargeTopAppBar(
        modifier = scrollBehavior?.nestedScrollConnection?.let {
            Modifier.nestedScroll(connection = it)
        } ?: Modifier,
        title = { Text(text) },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = colorResource(id = R.color.appBarColor),
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            onNavigationButtonClick?.takeIf { showBackButton }?.let { action ->
                IconButton(onClick = { action() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.navigation_item_accessibility),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            } ?: run {
                onHamburgerButtonClick?.takeIf { showHamburgerButton }?.let { action ->
                    IconButton(onClick = { action() }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(id = R.string.menu_item_accessibility),
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior,
    )
}
