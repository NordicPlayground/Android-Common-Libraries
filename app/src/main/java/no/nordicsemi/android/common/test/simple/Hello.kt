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

package no.nordicsemi.android.common.test.simple

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.navigation.createDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.navigation.defineDialogDestination
import no.nordicsemi.android.common.navigation.viewmodel.SimpleNavigationViewModel
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.ui.view.FloatingActionMenu
import no.nordicsemi.android.common.ui.view.FloatingActionMenuItem
import no.nordicsemi.android.common.ui.view.FloatingActionMenuItemSecondary

/**
 * This is an example of a simple destination.
 *
 * A simple destination does not take any parameters and does not return any result.
 */
val Hello = createDestination<Int, Unit>("hello")

private val HelloDestination = defineDestination(Hello) {
    val vm: SimpleNavigationViewModel = hiltViewModel()
    val param = vm.parameterOf(Hello)

    HelloScreen(
        param = param,
        onShowDialog = { vm.navigateTo(HelloDialog, "Hello World!") },
        modifier = Modifier.fillMaxWidth(),
    )
}

val HelloDialog = createDestination<String, Unit>("hello-dialog")

private val HelloDialogDestination = defineDialogDestination(HelloDialog) {
    val vm: SimpleNavigationViewModel = hiltViewModel()
    val param = vm.parameterOf(HelloDialog)

    // This should not be AlertDialog, but AlertDialogContent. This is already wrapped in Dialog.
    // Unfortunately, the AlertDialogContent is internal.
    AlertDialog(
        onDismissRequest = { vm.navigateUp() },
        title = { Text(text = param) },
        text = { Text(text = "This is a dialog.") },
        confirmButton = {
            TextButton(onClick = { vm.navigateUp() }) {
                Text(text = "OK")
            }
        },
    )
}

val HelloDestinations = HelloDestination + HelloDialogDestination + ScannerDestination

@Composable
private fun HelloScreen(
    param: Int,
    onShowDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionMenu(
                onDismissRequest = { showDialog = false },
                expanded = showDialog,
                menuContent = {
                    FloatingActionMenuItemSecondary(
                        label = "Clone Tag",
                        imageVector = Icons.Default.CopyAll,
                        onClick = { /*TODO*/ }
                    )
                    FloatingActionMenuItemSecondary(
                        label = "Existing Records",
                        imageVector = Icons.Default.Restore,
                        onClick = { /*TODO*/ }
                    )
                    FloatingActionMenuItem(
                        label = "New Record",
                        imageVector = Icons.Default.Add,
                        onClick = { /*TODO*/ }
                    )
                }
            ) {
                ExtendedFloatingActionButton(
                    text = { Text(text = "Add Record") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(id = R.string.hello))

            Text(text = stringResource(id = R.string.hello_param, param))

            Button(onClick = onShowDialog) {
                Text(text = stringResource(id = R.string.action_dialog))
            }
        }
    }
}

@Preview
@Composable
private fun SimpleContentPreview() {
    NordicTheme {
        HelloScreen(
            param = 0,
            onShowDialog = {},
        )
    }
}