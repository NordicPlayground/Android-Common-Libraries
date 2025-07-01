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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import no.nordicsemi.android.common.scanner.ScanFilterState
import no.nordicsemi.kotlin.ble.client.android.CentralManager
import no.nordicsemi.kotlin.ble.client.exception.BluetoothUnavailableException
import no.nordicsemi.kotlin.ble.client.exception.ScanningException
import no.nordicsemi.kotlin.ble.core.exception.ManagerClosedException
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
internal class ScannerViewModel @Inject constructor(
    private val centralManager: CentralManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private var timeout: Duration = Duration.INFINITE
    private var wasScanningStarted = false

    /** The scanning job. */
    private var scanning: Job? = null
    /** The filters applied to the scan results. */
    private lateinit var filterState: ScanFilterState
    /** All scanner results. */
    private var scanResults: MutableList<ScannedPeripheral> = mutableListOf()

    /**
     * Starts scanning for devices.
     *
     * This method works only once. If you want to restart the scan, call [reload] instead.
     *
     * @param timeout The duration after which the scan will stop. Defaults to [Duration.INFINITE].
     * @throws SecurityException If the app does not have the required permissions to scan for devices.
     * @throws ManagerClosedException If the [CentralManager] is closed.
     * @throws BluetoothUnavailableException If Bluetooth is not available on the device.
     * @throws ScanningException If the scan could not be started due to an error.
     */
    fun initiateScanning(
        timeout: Duration = Duration.INFINITE,
    ) {
        this.timeout = timeout

        if (!wasScanningStarted) {
            startScanning()
            wasScanningStarted = true
        }
    }

    private fun startScanning() {
        if (scanning != null) {
            return
        }

        scanning = centralManager.scan(
            timeout = timeout,
            filter = filterState.filter,
        )
            // Update the scanning state to loading when the scan starts.
            .onStart {
                _uiState.update {
                    it.copy(
                        isScanning = true,
                        scanningState = ScanningState.DevicesDiscovered(
                            result = emptyList(),
                        )
                    )
                }
            }
            // Filter out the scan results based on the provided filter in the scanResultFilter.
            .filter { it.isConnectable }
            .onEach { scanResult ->
                // Check if the device is already in the list.
                val existingIndex = scanResults.indexOfFirst { scanResult.peripheral == it.peripheral }
                if (existingIndex != -1) {
                    val previousHighestRssi = scanResults[existingIndex].highestRssi
                    val updatedPeripheral = ScannedPeripheral(scanResult, previousHighestRssi)
                    scanResults[existingIndex] = updatedPeripheral

                    val passes = filterState.passes(updatedPeripheral)

                    // Refresh the list without filtering or sorting to avoid flickering.
                    if (passes) {
                        _uiState.update { state ->
                            var list = (state.scanningState as ScanningState.DevicesDiscovered).result
                            val index = list.indexOfFirst { it.peripheral == updatedPeripheral.peripheral }
                            if (index == -1) {
                                list = (list + updatedPeripheral).sortedWith(filterState.activeSortingOption.comparator)
                            } else {
                                val updatedList = list.toMutableList()
                                updatedList[index] = updatedPeripheral
                                list = updatedList
                            }
                            state.copy(
                                isScanning = true,
                                scanningState = ScanningState.DevicesDiscovered(
                                    result = list
                                )
                            )
                        }
                    }
                } else {
                    val newPeripheral = ScannedPeripheral(scanResult)
                    scanResults.add(newPeripheral)

                    if (!filterState.passes(newPeripheral)) {
                        return@onEach
                    }

                    // A new device was discovered, update the UI state with the new list.
                    _uiState.update {
                        val list = (it.scanningState as ScanningState.DevicesDiscovered).result
                        it.copy(
                            isScanning = true,
                            scanningState = ScanningState.DevicesDiscovered(
                                result = (list + newPeripheral)
                                    .sortedWith(filterState.activeSortingOption.comparator),
                            )
                        )
                    }
                }
            }
            // Update the scanning state when the scan is completed.
            .onCompletion {
                _uiState.update { it.copy(isScanning = false) }
                scanning?.cancel()
                scanning = null
            }
            .catch { throwable ->
                _uiState.update {
                    it.copy(
                        isScanning = false,
                        scanningState = ScanningState.Error(throwable.message)
                    )
                }
                scanning?.cancel()
                scanning = null
                wasScanningStarted = false
            }
            .launchIn(viewModelScope)
    }

    fun reload() {
        scanResults.clear()
        _uiState.update {
            it.copy(
                scanningState = ScanningState.DevicesDiscovered(
                    result = emptyList(),
                )
            )
        }
        startScanning()
    }

    /**
     * Sets the filters for the scanner.
     */
    fun setFilterState(state: ScanFilterState) {
        println("AAA Setting filter state")
        filterState = state
        _uiState.update {
            it.copy(
                scanningState = ScanningState.DevicesDiscovered(
                    result = scanResults
                        .applyFilter(state)
                        .sortedWith(state.activeSortingOption.comparator),
                )
            )
        }
    }
}

private fun ScanFilterState.passes(peripheral: ScannedPeripheral): Boolean {
    // Check if the peripheral passes all dynamic filters.
    return dynamicFilters.withIndex().all { (index, filter) ->
        filter.predicate(isFilterSelected(index), peripheral.latestScanResult, peripheral.highestRssi)
    }
}

private fun List<ScannedPeripheral>.applyFilter(state: ScanFilterState): List<ScannedPeripheral> {
    // Note: This returns the same list. All equal will return true,
    //       even if the list has new items added.
    //       Fortunately, we always sort the list after applying the filter,
    //       so the list is copied to a new one which triggers the recomposition.
    if (state.dynamicFilters.isEmpty()) return this

    // Apply all filters.
    return this
        .filter { peripheral ->
            state.passes(peripheral)
        }
        .distinct()
}