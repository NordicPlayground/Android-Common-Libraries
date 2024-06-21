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

package no.nordicsemi.android.common.permissions.wifi

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.android.common.permissions.wifi.utils.WifiPermissionNotAvailableReason
import no.nordicsemi.android.common.permissions.wifi.utils.WifiPermissionState
import no.nordicsemi.android.common.permissions.wifi.view.WifiDisabledView
import no.nordicsemi.android.common.permissions.wifi.view.WifiNotAvailableView
import no.nordicsemi.android.common.permissions.wifi.view.WifiPermissionRequiredView
import no.nordicsemi.android.common.permissions.wifi.viewmodel.PermissionViewModel


/**
 * Composable that requests Wi-Fi permission.
 *
 * @param onChanged Callback that is called when the Wi-Fi state changes.
 * @param contentWithoutWifi The content to display when Wi-Fi is not available.
 * @param content The content to display when Wi-Fi is available.
 */
@Composable
fun RequireWifi(
    onChanged: (Boolean) -> Unit = {},
    contentWithoutWifi: @Composable (WifiPermissionNotAvailableReason) -> Unit = {
        NoWifiView(reason = it)
    },
    content: @Composable () -> Unit,
) {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val state by viewModel.wifiState.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        onChanged(state is WifiPermissionState.Available)
    }

    when (val s = state) {
        WifiPermissionState.Available -> content()

        is WifiPermissionState.NotAvailable -> {
            contentWithoutWifi(s.reason)
        }
    }
}


/**
 * Composable that requires Wi-Fi to be enabled.
 *
 * @param onChanged Callback that is called when the Wi-Fi state changes.
 * @param content The content to display when Wi-Fi is enabled.
 */
@Composable
fun RequireWifiEnabled(
    onChanged: (Boolean) -> Unit = {},
    content: @Composable () -> Unit,
) {

    val viewModel = hiltViewModel<PermissionViewModel>()
    val state by viewModel.wifiState.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        onChanged(state is WifiPermissionState.Available)
    }

    when( val s = state) {
        is WifiPermissionState.NotAvailable -> {
            if(s.reason == WifiPermissionNotAvailableReason.DISABLED) {
                WifiDisabledView()
            }
        }
        else -> {
            content()
        }
    }
}

@Composable
private fun NoWifiView(
    reason: WifiPermissionNotAvailableReason,
) {
    when (reason) {
        WifiPermissionNotAvailableReason.NOT_AVAILABLE -> WifiNotAvailableView()
        WifiPermissionNotAvailableReason.PERMISSION_REQUIRED ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                WifiPermissionRequiredView()
            }

        WifiPermissionNotAvailableReason.DISABLED -> WifiDisabledView()
    }
}