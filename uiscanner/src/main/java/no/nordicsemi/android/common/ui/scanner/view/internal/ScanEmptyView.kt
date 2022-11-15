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

package no.nordicsemi.android.common.ui.scanner.view.internal

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.core.parseBold
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.WarningView
import no.nordicsemi.android.common.ui.scanner.R

@Composable
internal fun ScanEmptyView(
    requireLocation: Boolean,
) {
    WarningView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        imageVector = Icons.Default.BluetoothSearching,
        title = stringResource(id = R.string.no_device_guide_title),
        hint = stringResource(id = R.string.no_device_guide_info) + if (requireLocation) {
            "\n\n" + stringResource(id = R.string.no_device_guide_location_info)
        } else {
            ""
        }.parseBold(),
        hintTextAlign = TextAlign.Justify,
    ) {
        if (requireLocation) {
            val context = LocalContext.current
            Button(onClick = { openLocationSettings(context) }) {
                Text(text = stringResource(id = R.string.action_location_settings))
            }
        }
    }
}

private fun openLocationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

@Preview
@Composable
private fun ScanEmptyViewPreview_RequiredLocation() {
    NordicTheme {
        ScanEmptyView(
            requireLocation = true,
        )
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun ScanEmptyViewPreview() {
    NordicTheme {
        ScanEmptyView(
            requireLocation = false,
        )
    }
}