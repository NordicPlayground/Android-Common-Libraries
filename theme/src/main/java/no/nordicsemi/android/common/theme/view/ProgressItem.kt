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

package no.nordicsemi.android.common.theme.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.R

enum class ProgressItemStatus {
    DISABLED, WORKING, SUCCESS, ERROR
}

@Composable
fun ProgressItem(
    text: String,
    status: ProgressItemStatus,
    modifier: Modifier = Modifier,
    iconRightPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(status.toImageRes()),
            contentDescription = null,
            tint = status.toIconColor(),
        )

        Spacer(modifier = Modifier.width(iconRightPadding))

        Column {
            ProvideTextStyle(value = MaterialTheme.typography.bodyMedium) {
                Text(
                    text = text,
                    color = status.toTextColor(),
                )
                content()
            }
        }
    }
}

@Composable
private fun ProgressItemStatus.toIconColor(): Color {
    return when (this) {
        ProgressItemStatus.DISABLED -> MaterialTheme.colorScheme.surfaceVariant
        ProgressItemStatus.WORKING -> LocalContentColor.current
        ProgressItemStatus.SUCCESS -> colorResource(id = R.color.nordicGrass)
        ProgressItemStatus.ERROR -> MaterialTheme.colorScheme.error
    }
}

@Composable
private fun ProgressItemStatus.toTextColor(): Color {
    return when (this) {
        ProgressItemStatus.DISABLED -> LocalContentColor.current.copy(alpha = 0.38f)
        ProgressItemStatus.WORKING,
        ProgressItemStatus.SUCCESS,
        ProgressItemStatus.ERROR -> LocalContentColor.current
    }
}

@DrawableRes
private fun ProgressItemStatus.toImageRes(): Int {
    return when (this) {
        ProgressItemStatus.DISABLED -> R.drawable.ic_dot
        ProgressItemStatus.WORKING -> R.drawable.ic_arrow_right
        ProgressItemStatus.SUCCESS -> R.drawable.ic_check
        ProgressItemStatus.ERROR -> R.drawable.ic_cross
    }
}

@Preview
@Composable
private fun ProgressItemPreview_Working() {
    NordicTheme {
        ProgressItem(
            text = "Uploading",
            status = ProgressItemStatus.WORKING,
        ) {
            LinearProgressIndicator(
                progress = { 0.3f },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "30%",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview
@Composable
private fun ProgressItemPreview_Success() {
    NordicTheme {
        ProgressItem(
            text = "Completed",
            status = ProgressItemStatus.SUCCESS,
        )
    }
}

@Preview
@Composable
private fun ProgressItemPreview_Disabled() {
    NordicTheme {
        ProgressItem(
            text = "Disabled",
            status = ProgressItemStatus.DISABLED,
        )
    }
}

@Preview
@Composable
private fun ProgressItemPreview_Error() {
    NordicTheme {
        ProgressItem(
            text = "Error: Too bad",
            status = ProgressItemStatus.ERROR,
        )
    }
}