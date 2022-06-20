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

package no.nordicsemi.ui.scanner.scanner.viewmodel

import android.os.ParcelUuid
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.LocalDataProvider
import no.nordicsemi.ui.scanner.scanner.repository.AllDevices
import no.nordicsemi.ui.scanner.scanner.repository.DevicesRepository
import no.nordicsemi.ui.scanner.scanner.repository.DevicesScanFilter
import no.nordicsemi.ui.scanner.scanner.repository.SuccessResult
import javax.inject.Inject

private const val FILTER_RSSI = -50 // [dBm]

@HiltViewModel
internal class ScannerViewModel @Inject constructor(
    val dataProvider: LocalDataProvider,
    private val devicesRepository: DevicesRepository,
) : ViewModel() {

    private var uuid: ParcelUuid? = null

    val config = MutableStateFlow(
        DevicesScanFilter(
            filterUuidRequired = true,
            filterNearbyOnly = false,
            filterWithNames = true
        )
    )

    val devices = config.combine(devicesRepository.getDevices()) { config, result ->
        val devices = (result.discoveredDevices as? SuccessResult<List<DiscoveredBluetoothDevice>>)?.let {
            val newItems = it.value.filter {
                if (uuid == null) {
                    true
                } else {
                    config.filterUuidRequired == false || it.scanResult?.scanRecord?.serviceUuids?.contains(uuid) ?: false
                }
            }.filter {
                !config.filterNearbyOnly || it.rssi >= FILTER_RSSI
            }.filter {
                !config.filterWithNames || !it.displayName().isNullOrBlank()
            }
            SuccessResult(newItems.filter { !result.bondedDevices.contains(it) })
        } ?: result.discoveredDevices
        result.copy(discoveredDevices = devices)
    }.stateIn(viewModelScope, SharingStarted.Lazily, AllDevices())

    fun setFilterUuid(uuid: ParcelUuid?) {
        this.uuid = uuid
        if (uuid == null) {
            config.value = config.value.copy(filterUuidRequired = null)
        }
    }

    fun filterByUuid(uuidRequired: Boolean) {
        config.value = config.value.copy(filterUuidRequired = uuidRequired)
    }

    fun filterByDistance(nearbyOnly: Boolean) {
        config.value = config.value.copy(filterNearbyOnly = nearbyOnly)
    }

    fun filterByName(nameRequired: Boolean) {
        config.value = config.value.copy(filterWithNames = nameRequired)
    }

    override fun onCleared() {
        super.onCleared()
        devicesRepository.clear()
    }
}
