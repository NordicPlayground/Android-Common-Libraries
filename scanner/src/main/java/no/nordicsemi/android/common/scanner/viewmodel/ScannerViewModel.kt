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
import no.nordicsemi.android.common.scanner.data.CustomFilter
import no.nordicsemi.android.common.scanner.data.Filter
import no.nordicsemi.android.common.scanner.data.GroupByName
import no.nordicsemi.android.common.scanner.data.OnFilterReset
import no.nordicsemi.android.common.scanner.data.OnFilterSelected
import no.nordicsemi.android.common.scanner.data.OnReloadScanResults
import no.nordicsemi.android.common.scanner.data.OnScanResultSelected
import no.nordicsemi.android.common.scanner.data.OnlyBonded
import no.nordicsemi.android.common.scanner.data.OnlyNearby
import no.nordicsemi.android.common.scanner.data.OnlyWithNames
import no.nordicsemi.android.common.scanner.data.SortBy
import no.nordicsemi.android.common.scanner.data.SortType
import no.nordicsemi.android.common.scanner.data.UiEvent
import no.nordicsemi.android.common.scanner.data.WithServiceUuid
import no.nordicsemi.kotlin.ble.client.android.CentralManager
import no.nordicsemi.kotlin.ble.client.android.ScanResult
import no.nordicsemi.kotlin.ble.client.android.exception.ScanningFailedToStartException
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * This class is responsible for managing the ui states of the scanner screen.
 *
 * @param isScanning True if the scanner is scanning.
 * @param scanningState The current scanning state.
 */
internal data class UiState(
    val isScanning: Boolean = false,
    val scanningState: ScanningState = ScanningState.Loading,
    val isGroupByNameEnabled: Boolean = false,
)

private const val FILTER_RSSI = -50 // [dBm]

@OptIn(ExperimentalUuidApi::class)
@HiltViewModel
internal class ScannerViewModel @Inject constructor(
    private val centralManager: CentralManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    private var job: Job? = null
    private var uuid: Uuid? = null

    private val _scanResultFilter = MutableStateFlow<List<Filter>>(emptyList())

    private val _originalScanResults = MutableStateFlow<List<ScanResult>>(emptyList())

    init {
        _scanResultFilter.onEach { filters ->
            // Apply the filter to the scan results.
            val originalResults = _originalScanResults.value
            val filteredResults = originalResults.applyFilter(filters)
            _uiState.update {
                it.copy(
                    scanningState = ScanningState.DevicesDiscovered(
                        result = filteredResults,
                        filters = filters,
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    @OptIn(ExperimentalUuidApi::class)
    fun startScanning(
        scanDuration: Long = 2000L,
    ) {
        job?.cancel()
        job = centralManager.scan(scanDuration.milliseconds) { uuid?.let { ServiceUuid(it) } }
            // Filter out the scan results based on the provided filter in the scanResultFilter.
            .filter { it.isConnectable }
            .onStart {
                // Update the scanning state to loading when the scan starts.
                _uiState.update {
                    it.copy(
                        isScanning = true,
                        scanningState = ScanningState.DevicesDiscovered(
                            result = emptyList(),
                            filters = _scanResultFilter.value
                        )
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
                    val result = (scanResults + scanResult)
                    _originalScanResults.value = result
                    _uiState.update {
                        it.copy(
                            scanningState = ScanningState.DevicesDiscovered(
                                result = result.applyFilter(_scanResultFilter.value),
                                filters = _scanResultFilter.value
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
            .catch { throwable ->
                (throwable as? ScanningFailedToStartException)?.let { exception ->
                    _uiState.update {
                        it.copy(
                            isScanning = false,
                            scanningState = ScanningState.Error(exception.message)
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun List<ScanResult>.applyFilter(filters: List<Filter>): List<ScanResult> {
        if (filters.isEmpty()) return this
        val sortBy = filters.filterIsInstance<SortBy>().firstOrNull()

        // Apply all filters to all options except SortBy
        val filtered = this.filter { scanResult ->
            filters.filterNot { it is SortBy }.all { it.filter(scanResult) }
        }.distinct()

        return when (sortBy?.sortType) {
            SortType.RSSI -> filtered.sortedByDescending { it.rssi }
            SortType.ALPHABETICAL -> filtered.sortedWith(
                compareBy(nullsLast()) { it.peripheral.name }
            )

            null -> filtered
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onClick(event: UiEvent) {
        when (event) {
            OnReloadScanResults -> {
                _uiState.update {
                    it.copy(
                        scanningState = ScanningState.DevicesDiscovered(
                            result = emptyList(),
                            filters = _scanResultFilter.value
                        )
                    )
                }
                startScanning()
            }

            is OnScanResultSelected -> {
                // TODO: Handle the scan result selection.
                println("Selected scan result: ${event.device}")
            }

            OnFilterReset -> {
                _scanResultFilter.update { emptyList() }
                val originalResults = _originalScanResults.value
                _uiState.update {
                    it.copy(
                        isGroupByNameEnabled = false,
                        scanningState = ScanningState.DevicesDiscovered(
                            result = originalResults,
                            filters = _scanResultFilter.value
                        )
                    )
                }
            }

            is OnFilterSelected -> {
                // Update the filter list with the selected filter.
                val currentFilter = _scanResultFilter.value.toMutableList()
                when (event.filter) {
                    is CustomFilter,
                    is OnlyBonded,
                    is OnlyNearby,
                    is OnlyWithNames,
                        // todo: remove the service uuid from here later. It should go on the scan result.
                    is WithServiceUuid -> {
                        currentFilter.addOrRemove(event.filter)
                        _scanResultFilter.update {
                            currentFilter
                        }
                    }

                    is GroupByName -> {
                        val updatedFilter =
                            currentFilter.filterNot { it is GroupByName }
                                .toMutableList()
                        _uiState.update {
                            it.copy(isGroupByNameEnabled = true)
                        }
                        updatedFilter.add(event.filter)
                        _scanResultFilter.update { updatedFilter }
                    }

                    is SortBy -> {
                        val updatedFilter =
                            currentFilter.filterNot { it is SortBy }.toMutableList()
                        updatedFilter.add(event.filter)
                        _scanResultFilter.update { updatedFilter }

                    }
                }
            }
        }
    }

    /**
     * Sets the filters for the scanner.
     */
    fun setFilters(filters: List<Filter>) {
        filters.forEach { filter ->
            when (filter) {
                // If uuid is set, it will be used to filter the scan results.
                is WithServiceUuid -> uuid = filter.uuid
                else -> {
                    // For other filters, we will add or remove them from the filter list.
                    val currentFilter = _scanResultFilter.value.toMutableList()
                    currentFilter.addOrRemove(filter)
                    _scanResultFilter.update {
                        currentFilter
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
}