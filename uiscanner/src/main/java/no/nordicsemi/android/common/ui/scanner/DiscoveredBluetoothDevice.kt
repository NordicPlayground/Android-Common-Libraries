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

package no.nordicsemi.android.common.ui.scanner

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.common.ui.scanner.bonding.repository.BondingState

@Suppress("unused")
@SuppressLint("MissingPermission")
@Parcelize
data class DiscoveredBluetoothDevice(
    val device: BluetoothDevice,
    val scanResult: ScanResult? = null,
    val name: String? = null,
    val lastScanResult: ScanResult? = null,
    val rssi: Int = 0,
    val previousRssi: Int = 0,
    val highestRssi: Int = 0
) : Parcelable {
    fun hasRssiLevelChanged(): Boolean {
        val newLevel =
            if (rssi <= 10) 0 else if (rssi <= 28) 1 else if (rssi <= 45) 2 else if (rssi <= 65) 3 else 4
        val oldLevel =
            if (previousRssi <= 10) 0 else if (previousRssi <= 28) 1 else if (previousRssi <= 45) 2 else if (previousRssi <= 65) 3 else 4
        return newLevel != oldLevel
    }

    fun provisioningData(): ProvisioningData? {
        return scanResult?.let { ProvisioningData.create(it) }
    }

    fun update(scanResult: ScanResult): DiscoveredBluetoothDevice {
        return copy(
            device = scanResult.device,
            lastScanResult = scanResult,
            name = scanResult.scanRecord?.deviceName,
            previousRssi = rssi,
            rssi = scanResult.rssi,
            highestRssi = if (highestRssi > rssi) highestRssi else rssi
        )
    }

    fun matches(scanResult: ScanResult): Boolean {
        return device.address == scanResult.device.address
    }

    fun getBondingState(): BondingState {
        return when (device.bondState) {
            BluetoothDevice.BOND_BONDED -> BondingState.BONDED
            BluetoothDevice.BOND_BONDING -> BondingState.BONDING
            else -> BondingState.NONE
        }
    }

    fun createBond() {
        device.createBond()
    }

    fun displayName(): String? {
        return when {
            name?.isNotEmpty() == true -> name
            device.name?.isNotEmpty() == true -> device.name
            else -> null
        }
    }

    fun address(): String {
        return device.address
    }

    fun displayAddress(): String {
        return when {
            else -> device.address
        }
    }

    fun displayNameOrAddress(): String {
        return displayName() ?: displayAddress()
    }

    override fun hashCode(): Int {
        return device.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is DiscoveredBluetoothDevice) {
            return device == other.device
        }
        return super.equals(other)
    }
}

fun ScanResult.toDiscoveredBluetoothDevice() = DiscoveredBluetoothDevice(
    device = device,
    scanResult = this,
    name = if (scanRecord != null) scanRecord!!.deviceName else null,
    previousRssi = rssi,
    rssi = rssi,
    highestRssi = rssi
)
