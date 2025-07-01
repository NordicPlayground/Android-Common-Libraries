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

package no.nordicsemi.android.common.test.simple

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.navigation.createDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.navigation.viewmodel.SimpleNavigationViewModel
import no.nordicsemi.android.common.scanner.DeviceSelected
import no.nordicsemi.android.common.scanner.ScannerScreen
import no.nordicsemi.android.common.scanner.ScanningCancelled
import no.nordicsemi.android.common.scanner.data.OnlyNearby
import no.nordicsemi.android.common.scanner.data.OnlyWithNames
import no.nordicsemi.android.common.scanner.data.WithServiceUuid
import no.nordicsemi.android.common.scanner.rememberFilterState
import no.nordicsemi.android.common.test.R
import no.nordicsemi.kotlin.ble.client.android.ScanResult
import no.nordicsemi.kotlin.ble.core.util.fromShortUuid
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

val ScannerDestinationId = createDestination<Unit, ScanResult>("ble-scanner")

@OptIn(ExperimentalUuidApi::class)
val ScannerDestination = defineDestination(ScannerDestinationId) {
    val navigationVM = hiltViewModel<SimpleNavigationViewModel>()

    ScannerScreen(
        cancellable = true,
        state = rememberFilterState(
            dynamicFilters = listOf(
                OnlyNearby(),
                OnlyWithNames(),
                WithServiceUuid(
                    title = R.string.filter_hrm,
                    icon = Icons.Default.MonitorHeart,
                    uuid = Uuid.fromShortUuid(0x180D), // Heart Rate Service UUID,
                    isInitiallySelected = true
                )
            )
        ),
        onResultSelected = {
            when (it) {
                is DeviceSelected -> {
                    navigationVM.navigateUpWithResult(ScannerDestinationId, it.scanResult)
                }

                ScanningCancelled -> {
                    navigationVM.navigateUp()
                }
            }
        }
    )
}
