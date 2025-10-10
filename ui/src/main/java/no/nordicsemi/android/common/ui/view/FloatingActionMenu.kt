/*
 * Copyright (c) 2024, Nordic Semiconductor
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

package no.nordicsemi.android.common.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import no.nordicsemi.android.common.ui.R

@Composable
fun FloatingActionMenu(
    onDismissRequest: () -> Unit,
    expanded: Boolean,
    menuContent: @Composable ColumnScope.() -> Unit,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    content: @Composable () -> Unit,
) {
    var bottomPadding by remember { mutableStateOf(0.dp) }
    var endPadding by remember { mutableStateOf(0.dp) }

    if (expanded) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
            ),
        ) {
            // By default, dialog's content is centered. We need to align it to the bottom-end,
            // where the FAB usually is.
            Box(contentAlignment = Alignment.BottomEnd) {
                // The scrim takes all the available space and closes the dialog when clicked.
                Scrim(onClose = onDismissRequest, modifier = Modifier.fillMaxSize())
                Column(
                    modifier = Modifier.padding(end = max(0.dp,endPadding - contentPadding.calculateEndPadding(LayoutDirection.Ltr))),
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        menuContent()
                    }
                    // The spacer positions the menu above the content.
                    Spacer(modifier = Modifier.height(max(0.dp, bottomPadding - contentPadding.calculateBottomPadding())))
                }
            }
        }
    }

    val density = LocalDensity.current
    val navigationBarHeight = WindowInsets.navigationBars.getBottom(density)
    Box(
        modifier = Modifier
            .onGloballyPositioned { layoutCoordinates ->
                with(density) {
                    val windowSize = layoutCoordinates.findRootCoordinates().size
                    val bottomOffset =
                        layoutCoordinates.positionInRoot().y + layoutCoordinates.size.height
                    val endOffset =
                        layoutCoordinates.positionInRoot().x + layoutCoordinates.size.width
                    bottomPadding =
                        windowSize.height.toDp() - bottomOffset.toDp() - navigationBarHeight.toDp()
                    endPadding = windowSize.width.toDp() - endOffset.toDp()
                }
            },
    ) {
        content()
    }
}

@Composable
fun FloatingActionMenuItem(
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    shape: Shape = FloatingActionButtonDefaults.shape,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
) {
    TextButton(
        onClick =onClick,
        colors = ButtonDefaults.textButtonColors().copy(
            contentColor = Color.White,
        ),
        shape = shape,
        contentPadding = contentPadding,
    ) {
        text()
        Spacer(modifier = Modifier.size(8.dp))
        icon()
    }
}

@Composable
fun FloatingActionMenuItem(
    label: String,
    imageVector: ImageVector,
    onClick: () -> Unit,
    shape: Shape = FloatingActionButtonDefaults.shape,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
) {
    FloatingActionMenuItem(
        text = { Text(text = label) },
        icon = {
            Icon(
                imageVector = imageVector,
                contentDescription = label,
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = shape,
                    )
                    .padding(16.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        },
        onClick = onClick,
        shape = shape,
        contentPadding = contentPadding,
    )
}

@Composable
fun FloatingActionMenuItem(
    label: String,
    painter: Painter,
    onClick: () -> Unit,
    shape: Shape = FloatingActionButtonDefaults.shape,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
) {
    FloatingActionMenuItem(
        text = { Text(text = label) },
        icon = {
            Icon(
                painter = painter,
                contentDescription = label,
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = shape,
                    )
                    .padding(16.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        },
        onClick = onClick,
        shape = shape,
        contentPadding = contentPadding,
    )
}

@Composable
fun FloatingActionMenuItemSecondary(
    label: String,
    imageVector: ImageVector,
    onClick: () -> Unit,
    shape: Shape = FloatingActionButtonDefaults.smallShape,
) {
    FloatingActionMenuItem(
        text = { Text(text = label) },
        icon = {
            Icon(
                imageVector = imageVector,
                contentDescription = label,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = shape,
                    )
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        onClick = onClick,
        shape = shape,
    )
}

@Composable
fun FloatingActionMenuItemSecondary(
    label: String,
    painter: Painter,
    onClick: () -> Unit,
    shape: Shape = FloatingActionButtonDefaults.smallShape,
) {
    FloatingActionMenuItem(
        text = { Text(text = label) },
        icon = {
            Icon(
                painter = painter,
                contentDescription = label,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = shape,
                    )
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        onClick = onClick,
        shape = shape,
    )
}

// Source: https://github.com/android/snippets/blob/1cf58aeef8bb496e8f957ddba27519a63b50c48c/compose/snippets/src/main/java/com/example/compose/snippets/touchinput/pointerinput/TapAndPress.kt#L296
@Composable
private fun Scrim(onClose: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            // handle pointer input
            .pointerInput(onClose) { detectTapGestures { onClose() } }
            // handle accessibility services
            .semantics(mergeDescendants = true) {
                contentDescription = ""
                onClick {
                    onClose()
                    true
                }
            }
            // handle physical keyboard input
            .onKeyEvent {
                if (it.key == Key.Escape) {
                    onClose()
                    true
                } else {
                    false
                }
            }
    )
}

@Preview(showBackground = true, heightDp = 200)
@Composable
private fun FloatingActionMenuPreview() {
    MaterialTheme {
        Scaffold(
            floatingActionButton = {
                var showDialog by remember { mutableStateOf(false) }

                FloatingActionMenu(
                    onDismissRequest = { showDialog = false },
                    expanded = showDialog,
                    menuContent = {
                        FloatingActionMenuItem(
                            label = "Option 1",
                            painter = painterResource(R.drawable.baseline_add_24),
                            onClick = { /*TODO*/ }
                        )
                        FloatingActionMenuItem(
                            label = "Option 2",
                            painter = painterResource(R.drawable.ic_cross),
                            onClick = { /*TODO*/ }
                        )
                    }
                ) {
                    ExtendedFloatingActionButton(
                        text = { Text(text = "Menu") },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_add_24),
                                contentDescription = null,
                            )
                        },
                        // Open the dialog when the FAB is clicked.
                        onClick = { showDialog = true },
                        // Collapse the FAB when the dialog is shown.
                        expanded = !showDialog
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding))
        }
    }
}