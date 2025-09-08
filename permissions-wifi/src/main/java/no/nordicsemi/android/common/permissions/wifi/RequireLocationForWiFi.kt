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

@file:Suppress("unused")

package no.nordicsemi.android.common.permissions.wifi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.android.common.permissions.wifi.permissionState.WiFiPermissionState
import no.nordicsemi.android.common.permissions.wifi.view.LocationDisabledView
import no.nordicsemi.android.common.permissions.wifi.view.LocationPermissionRequiredView
import no.nordicsemi.android.common.permissions.wifi.viewmodel.PermissionViewModel

/**
 * A wrapper for composables that require location permission for Wi-Fi scanning.
 *
 * This composable will request the location permission if needed.
 *
 * @param onChanged A callback that will be called when the permission state changes.
 * @param contentWithoutLocation A composable that will be displayed when the location permission is not available.
 * @param content A composable that will be displayed when the location permission is available.
 */
@Composable
fun RequireLocationForWiFi(
    onChanged: (Boolean) -> Unit = {},
    contentWithoutLocation: @Composable (WiFiPermissionNotAvailableReason) -> Unit = { reason ->
        when (reason) {
            WiFiPermissionNotAvailableReason.DISABLED -> LocationDisabledView()
            else -> LocationPermissionRequiredView()
        }
    },
    content: @Composable () -> Unit,
) {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val state by viewModel.locationPermission.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        onChanged(
            state is WiFiPermissionState.Available ||
                    (state as WiFiPermissionState.NotAvailable).reason == WiFiPermissionNotAvailableReason.DISABLED
        )
    }

    when (val s = state) {
        WiFiPermissionState.Available -> content()
        is WiFiPermissionState.NotAvailable -> contentWithoutLocation(s.reason)
    }
}