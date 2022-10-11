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

package no.nordicsemi.android.common.permission

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.permission.util.Available
import no.nordicsemi.android.common.permission.util.FeatureNotAvailableReason
import no.nordicsemi.android.common.permission.util.NotAvailable
import no.nordicsemi.android.common.permission.view.BluetoothDisabledView
import no.nordicsemi.android.common.permission.view.BluetoothNotAvailableView
import no.nordicsemi.android.common.permission.view.BluetoothPermissionRequiredView
import no.nordicsemi.android.common.permission.viewmodel.PermissionViewModel

@Composable
fun RequireBluetooth(
    onChanged: (Boolean) -> Unit = {},
    contentWithoutBluetooth: @Composable (FeatureNotAvailableReason) -> Unit = {
        NoBluetoothView(reason = it)
    },
    content: @Composable () -> Unit,
) {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val state by viewModel.bluetoothState.collectAsState()

    onChanged(state is Available)

    when (val s = state) {
        Available -> content()
        is NotAvailable -> contentWithoutBluetooth(s.reason)
    }
}

@Composable
private fun NoBluetoothView(
    reason: FeatureNotAvailableReason,
) {
    when (reason) {
        FeatureNotAvailableReason.NOT_AVAILABLE -> BluetoothNotAvailableView()
        FeatureNotAvailableReason.PERMISSION_REQUIRED ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                BluetoothPermissionRequiredView()
            }
        FeatureNotAvailableReason.DISABLED -> BluetoothDisabledView()
    }
}
