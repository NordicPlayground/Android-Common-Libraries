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
import no.nordicsemi.android.common.scanner.data.AllowBondedScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNameAndAddressScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNameScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNearbyScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNonEmptyNameScanResultFilter
import no.nordicsemi.android.common.scanner.data.OnFilterReset
import no.nordicsemi.android.common.scanner.data.OnFilterSelected
import no.nordicsemi.android.common.scanner.data.OnReloadScanResults
import no.nordicsemi.android.common.scanner.data.OnScanResultSelected
import no.nordicsemi.android.common.scanner.data.OnSortBySelected
import no.nordicsemi.android.common.scanner.data.ScanResultFilter
import no.nordicsemi.android.common.scanner.data.SortScanResult
import no.nordicsemi.android.common.scanner.data.SortType
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

    private val _scanResultFilter = MutableStateFlow<List<ScanResultFilter>>(emptyList())
    val scanResultFilter = _scanResultFilter.asStateFlow()

    private val _originalScanResults = MutableStateFlow<List<ScanResult>>(emptyList())

    init {
        _scanResultFilter.onEach {
            // Apply the filter to the scan results.
            val originalResults = _originalScanResults.value
            val filteredResults = originalResults.applyFilter(it)
            _uiState.update {
                it.copy(
                    scanningState = ScanningState.DevicesDiscovered(
                        result = filteredResults,
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    fun startScanning(
        scanDuration: Long = 2000L,
    ) {
        job?.cancel()
        job = centralManager.scan(scanDuration.milliseconds)
            // Filter out the scan results based on the provided filter in the scanResultFilter.
            .filter { it.isConnectable }
            .onStart {
                // Update the scanning state to loading when the scan starts.
                _uiState.update {
                    it.copy(
                        isScanning = true,
                        scanningState = ScanningState.DevicesDiscovered(
                            emptyList()
                        ),
                    )
                }
            }
            .cancellable()
            .onEach { scanResult ->
                val scanResults =
                    _uiState.value.scanningState.let { state ->
                        if (state is ScanningState.DevicesDiscovered) state.result else emptyList()
                    }
                // Check if the device is already in the list.
                val isExistingDevice =
                    scanResults.firstOrNull { it.peripheral.address == scanResult.peripheral.address }
                // Add the device to the list if it is not already in the list, otherwise ignore it.
                if (isExistingDevice == null) {
                    // Apply the filter to the scan result in the reload scan results.
                    // Update the scanning state with the new scan result.
                    val result = scanResults + scanResult
                    _originalScanResults.value = result
                    _uiState.update {
                        it.copy(
                            scanningState = ScanningState.DevicesDiscovered(
                                result = result,
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
                scanningState = ScanningState.DevicesDiscovered(
                    result = emptyList(),
                ),

                )
        }
        startScanning()
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun List<ScanResult>.applyFilter(filters: List<ScanResultFilter>): List<ScanResult> {
        if (filters.isEmpty()) return this
        // Iterate through the filters and apply them to the scan results.
        // Save the filtered results in a new list.
        val newList = mutableListOf<ScanResult>()
        filters.forEach { filter ->
            when (filter) {
                is AllowAddressScanResultFilter -> {
                    // Filter the scan results based on the address.
                    newList += this.filter {
                        it.peripheral.address == filter.address
                    }
                }

                AllowBondedScanResultFilter -> {
                    // Filter the scan results based on the bonded state.
                    newList += this.filter {
                        it.peripheral.bondState.value == BondState.BONDED
                    }
                }

                is AllowNameAndAddressScanResultFilter -> {
                    // Filter the scan results based on the name and address.
                    newList += this.filter {
                        it.peripheral.name == filter.name && it.peripheral.address == filter.address
                    }
                }

                is AllowNameScanResultFilter -> {
                    // Filter the scan results based on the name.
                    newList += this.filter {
                        it.peripheral.name == filter.name
                    }
                }

                AllowNearbyScanResultFilter -> {
                    // Filter the scan results based on the RSSI value.
                    newList += this.filter {
                        it.rssi >= FILTER_RSSI
                    }
                }

                AllowNonEmptyNameScanResultFilter -> {
                    // Filter the scan results based on the non-empty name.
                    newList += this.filter {
                        it.peripheral.name != null && it.peripheral.name?.isNotEmpty() == true
                    }
                }

                is SortScanResult -> {
                    newList += when (filter.sortType) {
                        SortType.RSSI -> {
                            this.sortedByDescending { it.rssi }
                        }

                        SortType.ALPHABETICAL -> {
                            this.sortedBy { it.peripheral.name }
                        }
                    }
                }
            }
        }
        return newList.distinct()
    }

    fun onClick(event: UiClickEvent) {
        when (event) {
            is OnFilterSelected -> {
                // Update the filter list with the selected filter.
                val currentFilter = _scanResultFilter.value.toMutableList()
                // check if the items on the list is already  on the current filter list
                currentFilter.addOrRemove(event.filter)
                _scanResultFilter.update {
                    currentFilter
                }
            }

            OnReloadScanResults -> {
                _uiState.update {
                    it.copy(
                        scanningState = ScanningState.DevicesDiscovered(
                            emptyList(),
                        )
                    )
                }
                // Clear the original scan filters.
                _scanResultFilter.update { emptyList() }
                startScanning()
            }

            is OnScanResultSelected -> {
                // TODO: Handle the scan result selection.
                println("Selected scan result: ${event.device}")
            }

            is OnFilterReset -> {
                _scanResultFilter.update { emptyList() }
                val originalResults = _originalScanResults.value
                _uiState.update {
                    it.copy(
                        scanningState = ScanningState.DevicesDiscovered(
                            result = originalResults,
                        )
                    )
                }
            }

            is OnSortBySelected -> {
                // Remove the existing sort by filter from the list.
                _scanResultFilter.value.filterIsInstance<SortScanResult>().forEach {
                    _scanResultFilter.update { currentFilter ->
                        currentFilter - it
                    }
                }
                when (event.sortBy) {
                    SortType.RSSI -> {
                        _scanResultFilter.update { it + SortScanResult(SortType.RSSI) }
                    }

                    SortType.ALPHABETICAL -> {
                        _scanResultFilter.update { it + SortScanResult(SortType.ALPHABETICAL) }
                    }
                }
            }
        }
    }

}

private fun <T> MutableList<T>.addOrRemove(item: T) {
    if (contains(item)) {
        remove(item)
    } else {
        add(item)
    }
}