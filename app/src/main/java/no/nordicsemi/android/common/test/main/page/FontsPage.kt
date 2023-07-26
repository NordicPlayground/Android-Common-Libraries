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

package no.nordicsemi.android.common.test.main.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.PagerViewItem

val FontsPage = PagerViewItem("Fonts") {
    FontsScreen()
}

@Composable
private fun FontsScreen() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Text samples
        Text(
            text = stringResource(id = R.string.text_display_large),
            style = MaterialTheme.typography.displayLarge,
        )
        Text(
            text = stringResource(id = R.string.text_display_medium),
            style = MaterialTheme.typography.displayMedium,
        )
        Text(
            text = stringResource(id = R.string.text_display_small),
            style = MaterialTheme.typography.displaySmall,
        )
        Text(
            text = stringResource(id = R.string.text_title_large),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(id = R.string.text_title_medium),
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = stringResource(id = R.string.text_title_small),
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            text = stringResource(id = R.string.text_body_large),
            // style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = stringResource(id = R.string.text_body_medium),
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = stringResource(id = R.string.text_body_small),
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = stringResource(id = R.string.text_label_large),
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = stringResource(id = R.string.text_label_medium),
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = stringResource(id = R.string.text_label_small),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
private fun FontsPreview() {
    NordicTheme {
        FontsScreen()
    }
}