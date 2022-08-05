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

package no.nordicsemi.android.common.ui.scanner.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.ui.scanner.R
import no.nordicsemi.android.common.ui.scanner.repository.DevicesScanFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FilterView(
    config: DevicesScanFilter,
    onChanged: (DevicesScanFilter) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.appBarColor))
            .padding(start = 56.dp)
    ) {
        config.filterUuidRequired?.let {
            ElevatedFilterChip(
                selected = !it,
                onClick = { onChanged(config.copy(filterUuidRequired = !it)) },
                label = { Text(text = stringResource(id = R.string.filter_uuid),) },
                modifier = Modifier.padding(end = 8.dp),
                leadingIcon = {
                    if (!it) {
                        Icon(Icons.Default.Done, contentDescription = "")
                    } else {
                        Icon(Icons.Default.Widgets, contentDescription = "")
                    }
                },
            )
        }
        with(config.filterNearbyOnly) {
            ElevatedFilterChip(
                selected = this,
                onClick = { onChanged(config.copy(filterNearbyOnly = !this)) },
                label = { Text(text = stringResource(id = R.string.filter_nearby),) },
                modifier = Modifier.padding(end = 8.dp),
                leadingIcon = {
                    if (this) {
                        Icon(Icons.Default.Done, contentDescription = "")
                    } else {
                        Icon(Icons.Default.Wifi, contentDescription = "")
                    }
                },
                colors = FilterChipDefaults.elevatedFilterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
            )
        }
        with(config.filterWithNames) {
            ElevatedFilterChip(
                selected = this,
                onClick = { onChanged(config.copy(filterWithNames = !this)) },
                label = { Text(text = stringResource(id = R.string.filter_name),) },
                modifier = Modifier.padding(end = 8.dp),
                leadingIcon = {
                    if (this) {
                        Icon(Icons.Default.Done, contentDescription = "")
                    } else {
                        Icon(Icons.Default.Label, contentDescription = "")
                    }
                },
            )
        }
    }
}