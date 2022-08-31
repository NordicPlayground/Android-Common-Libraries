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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.permission.manager.BluetoothPermissionResult
import no.nordicsemi.android.common.permission.manager.InternetPermissionResult

//Intended for Internet & Bluetooth permission. Please be careful when extended.
@Composable
fun PermissionScreen(
    onNavigateBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val bluetoothPermissionState = viewModel.bluetoothPermission.collectAsState().value
    val internetPermissionState = viewModel.internetPermission.collectAsState().value

    when (bluetoothPermissionState) {
        BluetoothPermissionResult.LOCATION_PERMISSION_REQUIRED ->
            LocationPermissionRequiredScreen(onNavigateBack) { viewModel.refresh()}
        BluetoothPermissionResult.BLUETOOTH_PERMISSION_REQUIRED ->
            BluetoothPermissionRequiredScreen(onNavigateBack) { viewModel.refresh() }
        BluetoothPermissionResult.BLUETOOTH_NOT_AVAILABLE ->
            BluetoothNotAvailableScreen(onNavigateBack)
        BluetoothPermissionResult.BLUETOOTH_DISABLED ->
            BluetoothDisabledScreen(onNavigateBack)
        BluetoothPermissionResult.ALL_GOOD ->
            when (internetPermissionState) {
                InternetPermissionResult.INTERNET_DISABLED -> InternetNotAvailableScreen(onNavigateBack)
                InternetPermissionResult.ALL_GOOD -> content()
            }
    }
}

@Composable
fun BluetoothPermissionScreen(
    onNavigateBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val bluetoothPermissionState = viewModel.bluetoothPermission.collectAsState().value

    when (bluetoothPermissionState) {
        BluetoothPermissionResult.LOCATION_PERMISSION_REQUIRED ->
            LocationPermissionRequiredScreen(onNavigateBack) { viewModel.refresh() }
        BluetoothPermissionResult.BLUETOOTH_PERMISSION_REQUIRED ->
            BluetoothPermissionRequiredScreen(onNavigateBack) { viewModel.refresh() }
        BluetoothPermissionResult.BLUETOOTH_NOT_AVAILABLE ->
            BluetoothNotAvailableScreen(onNavigateBack)
        BluetoothPermissionResult.BLUETOOTH_DISABLED ->
            BluetoothDisabledScreen(onNavigateBack)
        BluetoothPermissionResult.ALL_GOOD ->
            content()
    }
}
