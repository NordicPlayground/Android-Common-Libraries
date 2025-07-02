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

import no.nordicsemi.kotlin.ble.client.android.Peripheral
import no.nordicsemi.kotlin.ble.client.android.ScanResult
import kotlin.math.max

/**
 * A record containing information about a scanned peripheral.
 *
 * @property peripheral The peripheral instance.
 * @property latestScanResult The latest scan result received from the peripheral.
 * @property highestRssi The highest RSSI value recorded for this peripheral during the scanning session.
 */
data class ScannedPeripheral(
    val peripheral: Peripheral,
    var latestScanResult: ScanResult,
    var highestRssi: Int,
) {
    constructor(scanResult: ScanResult, previousHighestRssi: Int = -128) : this(
        peripheral = scanResult.peripheral,
        latestScanResult = scanResult,
        highestRssi = max(previousHighestRssi, scanResult.rssi)
    )

    override fun equals(other: Any?): Boolean {
        return other is ScannedPeripheral &&
               other.peripheral == peripheral &&
               other.latestScanResult.rssi == latestScanResult.rssi
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

/** ScanningState represents the state of the scanning process. */
internal sealed interface ScanningState {

    /** Loading state. */
    data object Loading : ScanningState

    /**
     * Devices discovered state.
     *
     * @property result The list of discovered devices.
     */
    data class DevicesDiscovered(
        val result: List<ScannedPeripheral>,
    ) : ScanningState

    /**
     * Error state.
     *
     * @property error The error that occurred.
     */
    data class Error(val error: String?) : ScanningState
}

/**
 * This class is responsible for managing the ui states of the scanner screen.
 *
 * @property isScanning True if the scanner is scanning.
 * @property scanningState The current scanning state.
 */
internal data class UiState(
    val isScanning: Boolean = false,
    val scanningState: ScanningState = ScanningState.Loading,
)