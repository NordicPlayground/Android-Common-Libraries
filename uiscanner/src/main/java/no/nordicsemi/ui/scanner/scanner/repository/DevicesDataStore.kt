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

package no.nordicsemi.ui.scanner.scanner.repository

import android.bluetooth.BluetoothAdapter
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.toDiscoveredBluetoothDevice
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DevicesDataStore @Inject constructor(
    private val bleAdapter: BluetoothAdapter
) {

    val bondedDevices
        get() = bleAdapter.bondedDevices.map { DiscoveredBluetoothDevice(it) }
    val devices = mutableListOf<DiscoveredBluetoothDevice>()
    val data = MutableStateFlow(devices.toList())

    fun addNewDevice(scanResult: ScanResult) {
        devices.firstOrNull { it.device.address == scanResult.device.address }?.let {
            val i = devices.indexOf(it)
            devices.set(i, it.update(scanResult))
        } ?: scanResult.toDiscoveredBluetoothDevice().also { devices.add(it) }

        data.value = devices.toList()
    }

    fun clear() {
        devices.clear()
        data.value = devices
    }
}

internal data class DevicesScanFilter(
    val filterUuidRequired: Boolean?,
    val filterNearbyOnly: Boolean,
    val filterWithNames: Boolean
)
