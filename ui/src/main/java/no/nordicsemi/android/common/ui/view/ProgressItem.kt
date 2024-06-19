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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import no.nordicsemi.android.common.ui.R

enum class ProgressItemStatus {
    DISABLED, WORKING, SUCCESS, ERROR
}

@Composable
fun ProgressItem(
    text: String,
    status: ProgressItemStatus,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    iconRightPadding: Dp = 24.dp,
) {
    ProgressItem(
        status = status,
        modifier = modifier,
        verticalAlignment = verticalAlignment,
        iconRightPadding = iconRightPadding,
    ) {
        Text(text = text)
    }
}

@Composable
fun ProgressItem(
    status: ProgressItemStatus,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    iconRightPadding: Dp = 24.dp,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment,
    ) {
        Icon(
            painter = painterResource(status.toImageRes()),
            contentDescription = null,
            tint = status.toIconColor(),
        )

        Spacer(modifier = Modifier.width(iconRightPadding))

        Column(
            modifier = Modifier.padding(vertical = 2.dp)
        ) {
            content()
        }
    }
}

@Composable
fun StatusItem(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .then(modifier),
    ) {
        VerticalDivider(
            modifier = Modifier.padding(start = 10.dp, end = 34.dp)
        )
        Column(
            modifier = Modifier.padding(vertical = 2.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            content()
        }
    }
}

@Composable
private fun ProgressItemStatus.toIconColor(): Color {
    return when (this) {
        ProgressItemStatus.DISABLED -> MaterialTheme.colorScheme.surfaceVariant
        ProgressItemStatus.WORKING -> LocalContentColor.current
        ProgressItemStatus.SUCCESS -> colorResource(id = R.color.colorSuccess)
        ProgressItemStatus.ERROR -> MaterialTheme.colorScheme.error
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

@Preview(showBackground = true)
@Composable
private fun ProgressItemPreview_Working() {
    MaterialTheme {
        ProgressItem(
            status = ProgressItemStatus.ERROR,
        ) {
            Text(text = "Uploading...")
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

@Preview(showBackground = true)
@Composable
private fun ProgressItemPreview_Success() {
    MaterialTheme {
        ProgressItem(
            text = "Success!\nBut there's more!\nAnd even more!",
            status = ProgressItemStatus.SUCCESS,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressItemPreview_Disabled() {
    MaterialTheme {
        ProgressItem(
            text = "Disabled",
            status = ProgressItemStatus.DISABLED,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressItemPreview_Error() {
    MaterialTheme {
        ProgressItem(
            text = "Error: Too bad",
            status = ProgressItemStatus.ERROR,
        )
    }
}