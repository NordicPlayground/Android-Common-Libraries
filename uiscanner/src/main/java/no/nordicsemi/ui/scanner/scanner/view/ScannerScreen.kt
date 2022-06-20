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

package no.nordicsemi.ui.scanner.scanner.view

import android.os.ParcelUuid
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.navigation.doNothing
import no.nordicsemi.ui.scanner.R
import no.nordicsemi.ui.scanner.permissions.DeviceSelected
import no.nordicsemi.ui.scanner.permissions.NavigateUp
import no.nordicsemi.ui.scanner.permissions.PermissionsViewEvent
import no.nordicsemi.ui.scanner.scanner.repository.DevicesScanFilter
import no.nordicsemi.ui.scanner.scanner.viewmodel.ScannerViewModel
import no.nordicsemi.ui.scanner.ui.*

@Composable
internal fun ScannerScreen(
    uuid: ParcelUuid?,
    onEvent: (PermissionsViewEvent) -> Unit
) {
    val viewModel = hiltViewModel<ScannerViewModel>().apply {
        setFilterUuid(uuid)
    }
    val requireLocation = viewModel.dataProvider.locationState.collectAsState().value
    val config = viewModel.config.collectAsState().value

    Column {
        val dialogConfig = createConfig(config, onEvent)

        AppBar(stringResource(id = R.string.scanner_screen), dialogConfig.isRunning()) { onEvent(NavigateUp) }

        DevicesListView(requireLocation, dialogConfig)
    }
}

@Composable
private fun createConfig(
    config: DevicesScanFilter,
    onEvent: (PermissionsViewEvent) -> Unit
): StringListDialogConfig {

    val viewModel = hiltViewModel<ScannerViewModel>()
    val result = viewModel.devices.collectAsState().value

    val uuidIsCheckedFilter = config.filterUuidRequired
    val nearbyIsCheckedFilter = config.filterNearbyOnly
    val nameCheckedFilter = config.filterWithNames

    val uuidFilter = uuidIsCheckedFilter?.let {
        FilterItem(
            stringResource(id = R.string.filter_uuid),
            it
        ) {
            viewModel.filterByUuid(!uuidIsCheckedFilter)
        }
    }
    val nearbyFilter = FilterItem(
        stringResource(id = R.string.filter_nearby),
        nearbyIsCheckedFilter
    ) {
        viewModel.filterByDistance(!nearbyIsCheckedFilter)
    }
    val nameFilter = FilterItem(
        stringResource(id = R.string.filter_name),
        nameCheckedFilter
    ) {
        viewModel.filterByName(!nameCheckedFilter)
    }
    val filters = listOfNotNull(uuidFilter, nearbyFilter, nameFilter)

    return StringListDialogConfig(
        title = stringResource(id = R.string.devices).toAnnotatedString(),
        result = result,
        filterItems = filters,
        leftIcon = R.drawable.ic_bluetooth,
        onFilterItemCheckChanged = { filters[it].onClick() }
    ) {
        when (it) {
            FlowCanceled -> doNothing()
            is ItemSelectedResult -> onEvent(DeviceSelected(it.value))
        }
    }
}
