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

package no.nordicsemi.android.common.permissions.ble.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.permissions.ble.R
import no.nordicsemi.android.common.permissions.ble.viewmodel.PermissionViewModel
import no.nordicsemi.android.common.ui.view.WarningView

@Composable
internal fun LocationPermissionRequiredView() {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val context = LocalContext.current
    var permissionDenied by remember { mutableStateOf(viewModel.isLocationPermissionDeniedForever(context)) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel.markLocationPermissionRequested()
        permissionDenied = viewModel.isLocationPermissionDeniedForever(context)
        viewModel.refreshLocationPermission()
    }

    LocationPermissionRequiredView(
        permissionDenied = permissionDenied,
        onGrantClicked = {
            val requiredPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            launcher.launch(requiredPermissions)
        },
        onOpenSettingsClicked = { openPermissionSettings(context) },
    )
}

@Composable
internal fun LocationPermissionRequiredView(
    permissionDenied: Boolean,
    onGrantClicked: () -> Unit,
    onOpenSettingsClicked: () -> Unit,
) {
    WarningView(
        imageVector = Icons.Default.LocationOff,
        title = stringResource(id = R.string.location_permission_title),
        hint = stringResource(id = R.string.location_permission_info),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (!permissionDenied) {
            Button(onClick = onGrantClicked) {
                Text(text = stringResource(id = R.string.action_grant_permission))
            }
        } else {
            Button(onClick = onOpenSettingsClicked) {
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

@Preview(showBackground = true)
@Composable
private fun LocationPermissionRequiredView_Preview() {
    MaterialTheme {
        LocationPermissionRequiredView(
            permissionDenied = false,
            onGrantClicked = { },
            onOpenSettingsClicked = { },
        )
    }
}

