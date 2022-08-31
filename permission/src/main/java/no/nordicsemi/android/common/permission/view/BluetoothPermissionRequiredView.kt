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

package no.nordicsemi.android.common.permission.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.permission.R
import no.nordicsemi.android.common.permission.view.internal.BigIcon
import no.nordicsemi.android.common.permission.view.internal.Hint
import no.nordicsemi.android.common.permission.view.internal.Title
import no.nordicsemi.android.common.theme.view.NordicAppBar

@Composable
fun BluetoothPermissionRequiredScreen(
    onNavigateBack: () -> Unit,
    onPermissionChanged: () -> Unit
) {
    Column {
        NordicAppBar(stringResource(id = R.string.bluetooth_required_title), onNavigateBack)

        BluetoothPermissionRequiredView(onPermissionChanged)
    }
}

@SuppressLint("InlinedApi")
@Composable
fun BluetoothPermissionRequiredView(
    onPermissionChanged: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        BigIcon(imageVector = Icons.Default.BluetoothDisabled)

        Spacer(modifier = Modifier.size(16.dp))

        Title(text = stringResource(id = R.string.bluetooth_permission_title))

        Spacer(modifier = Modifier.size(16.dp))

        Hint(text = stringResource(id = R.string.bluetooth_permission_info))

        Spacer(modifier = Modifier.size(16.dp))

        val requiredPermissions = arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { onPermissionChanged() }

        val viewModel = hiltViewModel<PermissionViewModel>()
        val context = LocalContext.current
        if (!viewModel.isBluetoothScanPermissionDeniedForever(context as Activity)) {
            Button(onClick = { launcher.launch(requiredPermissions) }) {
                Text(text = stringResource(id = R.string.action_grant_permission))
            }
        } else {
            Button(onClick = { openPermissionSettings(context) }) {
                Text(text = stringResource(id = R.string.action_settings))
            }
        }
    }
}

private fun openPermissionSettings(context: Context) {
    ContextCompat.startActivity(
        context,
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ),
        null
    )
}

@Preview
@Composable
private fun BluetoothPermissionRequiredView_Preview() {
    BluetoothPermissionRequiredView { }
}
