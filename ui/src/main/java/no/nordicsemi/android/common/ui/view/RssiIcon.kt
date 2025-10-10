/*
 * Copyright (c) 2023, Nordic Semiconductor
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

package no.nordicsemi.android.common.ui.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.ui.R

private const val MEDIUM_RSSI = -80
private const val MAX_RSSI = -60

/**
 * A component that displays an icon and a text representing the RSSI value.
 *
 * @param rssi The RSSI value.
 */
@Composable
fun RssiIcon(rssi: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Image(
            painter = painterResource(id = getImageRes(rssi)),
            contentDescription = null
        )
        Text(
            text = stringResource(id = R.string.dbm, rssi),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@DrawableRes
private fun getImageRes(rssi: Int): Int {
    return when {
        rssi < MEDIUM_RSSI -> R.drawable.ic_signal_min
        rssi < MAX_RSSI -> R.drawable.ic_signal_medium
        else -> R.drawable.ic_signal_max
    }
}

@Preview(showBackground = true, widthDp = 500)
@Composable
private fun RssiIconPreview() {
    MaterialTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RssiIcon(rssi = -30)
            RssiIcon(rssi = -40)
            RssiIcon(rssi = -50)
            RssiIcon(rssi = -60)
            RssiIcon(rssi = -70)
            RssiIcon(rssi = -80)
            RssiIcon(rssi = -90)
            RssiIcon(rssi = -100)
        }
    }
}