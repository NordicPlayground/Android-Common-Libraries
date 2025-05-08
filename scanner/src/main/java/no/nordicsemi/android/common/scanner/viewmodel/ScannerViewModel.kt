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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import no.nordicsemi.android.common.scanner.repository.ScanningManager
import javax.inject.Inject

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

@HiltViewModel
internal class ScannerViewModel @Inject constructor(
    private val scanningManager: ScanningManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()
    private var job: Job? = null

    fun startScanning() {
        job?.cancel()
        job = scanningManager.scanDevices()
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
            .cancellable()
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

}