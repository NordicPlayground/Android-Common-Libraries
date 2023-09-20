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

package no.nordicsemi.android.common.permissions.ble.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.common.permissions.ble.util.BlePermissionNotAvailableReason
import no.nordicsemi.android.common.permissions.ble.util.BlePermissionState
import no.nordicsemi.android.common.permissions.ble.util.LocalDataProvider
import no.nordicsemi.android.common.permissions.ble.util.PermissionUtils
import javax.inject.Inject
import javax.inject.Singleton

private const val REFRESH_PERMISSIONS = "no.nordicsemi.android.common.permission.REFRESH_BLUETOOTH_PERMISSIONS"

@Singleton
class BluetoothStateManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataProvider = LocalDataProvider(context)
    private val utils = PermissionUtils(context, dataProvider)

    fun bluetoothState() = callbackFlow {
        trySend(getBluetoothPermissionState())

        val bluetoothStateChangeHandler = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                trySend(getBluetoothPermissionState())
            }
        }
        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(REFRESH_PERMISSIONS)
        }
        ContextCompat.registerReceiver(context, bluetoothStateChangeHandler, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
        awaitClose {
            context.unregisterReceiver(bluetoothStateChangeHandler)
        }
    }

    fun refreshPermission() {
        val intent = Intent(REFRESH_PERMISSIONS)
        context.sendBroadcast(intent)
    }

    fun markBluetoothPermissionRequested() {
        dataProvider.bluetoothPermissionRequested = true
    }

    fun isBluetoothScanPermissionDeniedForever(context: Context): Boolean {
        return utils.isBluetoothScanPermissionDeniedForever(context)
    }

    private fun getBluetoothPermissionState() = when {
        !utils.isBluetoothAvailable -> BlePermissionState.NotAvailable(
            BlePermissionNotAvailableReason.NOT_AVAILABLE)
        !utils.areNecessaryBluetoothPermissionsGranted -> BlePermissionState.NotAvailable(
            BlePermissionNotAvailableReason.PERMISSION_REQUIRED)
        !utils.isBleEnabled -> BlePermissionState.NotAvailable(
            BlePermissionNotAvailableReason.DISABLED)
        else -> BlePermissionState.Available
    }
}
