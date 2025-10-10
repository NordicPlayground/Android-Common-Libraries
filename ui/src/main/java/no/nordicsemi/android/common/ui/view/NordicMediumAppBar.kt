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

package no.nordicsemi.android.common.ui.view

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import no.nordicsemi.android.common.ui.R

@ExperimentalMaterial3Api
@Composable
fun NordicMediumAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onNavigationButtonClick: (() -> Unit)? = null,
    onHamburgerButtonClick: (() -> Unit)? = null,
    showBackButton: Boolean = onNavigationButtonClick != null,
    backButtonIcon: ImageVector,
    showHamburgerButton: Boolean = onHamburgerButtonClick != null,
    collapsedHeight: Dp = TopAppBarDefaults.LargeAppBarCollapsedHeight,
    expandedHeight: Dp = TopAppBarDefaults.LargeAppBarExpandedHeight,
    windowInsets: WindowInsets = WindowInsets.displayCutout
        .union(WindowInsets.statusBars)
        .union(WindowInsets.navigationBars)
        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    NordicMediumAppBar(
        title = title,
        modifier = modifier,
        onNavigationButtonClick = onNavigationButtonClick,
        onHamburgerButtonClick = onHamburgerButtonClick,
        showBackButton = showBackButton,
        backButtonIcon = rememberVectorPainter(image = backButtonIcon),
        showHamburgerButton = showHamburgerButton,
        collapsedHeight = collapsedHeight,
        expandedHeight = expandedHeight,
        windowInsets = windowInsets,
        scrollBehavior = scrollBehavior,
        actions = actions,
    )
}

@ExperimentalMaterial3Api
@Composable
fun NordicMediumAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onNavigationButtonClick: (() -> Unit)? = null,
    onHamburgerButtonClick: (() -> Unit)? = null,
    showBackButton: Boolean = onNavigationButtonClick != null,
    backButtonIcon: Painter = painterResource(id = R.drawable.baseline_arrow_back_24),
    showHamburgerButton: Boolean = onHamburgerButtonClick != null,
    collapsedHeight: Dp = TopAppBarDefaults.MediumAppBarCollapsedHeight,
    expandedHeight: Dp = TopAppBarDefaults.MediumAppBarExpandedHeight,
    windowInsets: WindowInsets = WindowInsets.displayCutout
        .union(WindowInsets.statusBars)
        .union(WindowInsets.navigationBars)
        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    MediumTopAppBar(
        title = title,
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.appBarColor),
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            onNavigationButtonClick?.takeIf { showBackButton }?.let { action ->
                IconButton(onClick = action) {
                    Icon(
                        painter = backButtonIcon,
                        contentDescription = stringResource(id = R.string.navigation_item_accessibility),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            } ?: run {
                onHamburgerButtonClick?.takeIf { showHamburgerButton }?.let { action ->
                    IconButton(onClick = action) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_menu_24),
                            contentDescription = stringResource(id = R.string.menu_item_accessibility),
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior,
        windowInsets = windowInsets,
        collapsedHeight = collapsedHeight,
        expandedHeight = expandedHeight,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun NordicMediumAppBarPreview() {
    MaterialTheme {
        NordicMediumAppBar(
            title = { Text(text = "Title") },
            actions = {
                IconButton(onClick = {}) {
                    Icon(painter = painterResource(R.drawable.baseline_add_24), contentDescription = "")
                }
                IconButton(onClick = {}) {
                    Icon(painter = painterResource(R.drawable.baseline_menu_24), contentDescription = "")
                }
            },
            onHamburgerButtonClick = {},
            showHamburgerButton = true,
        )
    }
}
