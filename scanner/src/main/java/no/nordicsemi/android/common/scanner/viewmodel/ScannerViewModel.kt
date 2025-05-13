/*
 * Copyright (c) 2025, Nordic Semiconductor
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

package no.nordicsemi.android.common.scanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import no.nordicsemi.android.common.scanner.data.AllowAddressScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowAllScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowBondedScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNameAndAddressScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNameScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNearbyScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNonEmptyNameScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowUuidScanResultFilter
import no.nordicsemi.android.common.scanner.data.OnFilterSelected
import no.nordicsemi.android.common.scanner.data.OnReloadScanResults
import no.nordicsemi.android.common.scanner.data.OnScanResultSelected
import no.nordicsemi.android.common.scanner.data.ScanResultFilter
import no.nordicsemi.android.common.scanner.data.UiClickEvent
import no.nordicsemi.kotlin.ble.client.android.CentralManager
import no.nordicsemi.kotlin.ble.client.android.ScanResult
import no.nordicsemi.kotlin.ble.core.BondState
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.uuid.ExperimentalUuidApi

/**
 * This class is responsible for managing the ui states of the scanner screen.
 *
 * @param isScanning True if the scanner is scanning.
 * @param scanningState The current scanning state.
 */
internal data class ScannerUiState(
    val isScanning: Boolean = false,
    val scanningState: ScanningState = ScanningState.Loading,
)

private const val FILTER_RSSI = -50 // [dBm]

@HiltViewModel
internal class ScannerViewModel @Inject constructor(
    private val centralManager: CentralManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()
    private var job: Job? = null

    private val _scanResultFilter = MutableStateFlow<ScanResultFilter>(AllowAllScanResultFilter)
    val scanResultFilterState = _scanResultFilter.asStateFlow()

    fun startScanning(
        scanDuration: Long = 2000L,
    ) {
        job?.cancel()
        job = centralManager.scan(scanDuration.milliseconds)

            // Filter out the scan results based on the provided filter in the scanResultFilter.
            .filter { it.isConnectable }
            .onStart {
                _uiState.update {
                    it.copy(
                        isScanning = true,
                        scanningState = ScanningState.DevicesDiscovered(emptyList()),
                    )
                }
            }
            .cancellable()
            .onEach { scanResult ->
                val scanResults =
                    _uiState.value.scanningState.let { state ->
                        if (state is ScanningState.DevicesDiscovered) state.result.applyFilter(
                            _scanResultFilter.value
                        ) else emptyList()
                    }
                // Check if the device is already in the list.
                val isExistingDevice =
                    scanResults.firstOrNull { it.peripheral.address == scanResult.peripheral.address }
                // Add the device to the list if it is not already in the list, otherwise ignore it.
                if (isExistingDevice == null) {
                    // Update the scanning state with the new scan result.
                    _uiState.update {
                        it.copy(
                            scanningState = ScanningState.DevicesDiscovered(
                                result = scanResults + scanResult
                            )
                        )
                    }
                }
            }
            // Update the scanning state when the scan is completed.
            .onCompletion {
                _uiState.update { it.copy(isScanning = false) }
                job?.cancel()
            }
            .catch { e ->
                _uiState.update {
                    it.copy(
                        isScanning = false,
                        scanningState = ScanningState.Error(e)
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun refreshScanning() {
        _uiState.update {
            it.copy(
                isScanning = true,
                scanningState = ScanningState.DevicesDiscovered(emptyList()),
            )
        }
        startScanning()
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun List<ScanResult>.applyFilter(filter: ScanResultFilter): List<ScanResult> {
        println("Filter: $filter")
        return when (filter) {
            is AllowAddressScanResultFilter -> {
                this.filter {
                    it.peripheral.address == filter.address
                }
            }

            AllowAllScanResultFilter -> this
            is AllowBondedScanResultFilter -> {
                this.filter {
                    it.peripheral.bondState.value == BondState.BONDED
                }
            }

            is AllowNameAndAddressScanResultFilter -> {
                this.filter {
                    it.peripheral.name == filter.name && it.peripheral.address == filter.address
                }
            }

            is AllowNameScanResultFilter -> {
                this.filter {
                    it.peripheral.name == filter.name
                }
            }

            AllowNearbyScanResultFilter -> {
                this.filter { it.rssi >= FILTER_RSSI } // TODO: check if this statement is correct.
            }

            AllowNonEmptyNameScanResultFilter -> {
                this.filter {
                    it.peripheral.name != null && it.peripheral.name?.isNotEmpty() == true
                }
            }

            is AllowUuidScanResultFilter -> {
                this.filter {
                    it.advertisingData.serviceUuids.contains(filter.uuid)
                }
            }
        }
    }

    fun onClick(event: UiClickEvent) {
        when (event) {
            is OnFilterSelected -> _scanResultFilter.value = event.filter
            OnReloadScanResults -> {
                _uiState.update {
                    it.copy(
                        scanningState = ScanningState.DevicesDiscovered(emptyList()),
                    )
                }
                startScanning()
            }

            is OnScanResultSelected -> {
                // TODO: Handle the scan result selection.
                println("Selected scan result: ${event.device}")
            }
        }
    }


}