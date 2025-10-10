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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.ui.R
import no.nordicsemi.android.common.ui.view.internal.BigIcon
import no.nordicsemi.android.common.ui.view.internal.Hint

/**
 * A warning view is a component that displays a warning icon, a title, and a hint.
 *
 * @param imageVector The vector icon to be displayed.
 * @param title The title of the warning.
 * @param hint The hint to be displayed.
 * @param modifier The modifier to be applied to the layout.
 * @param hintTextAlign The text alignment of the hint, defaults to [TextAlign.Center].
 * @param content The content to be displayed below the hint.
 */
@Composable
fun WarningView(
    imageVector: ImageVector,
    title: String,
    hint: String,
    modifier: Modifier = Modifier,
    hintTextAlign: TextAlign? = TextAlign.Center,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    WarningView(
        painterResource = rememberVectorPainter(image = imageVector),
        title = title,
        hint = AnnotatedString(text = hint),
        modifier = modifier,
        hintTextAlign = hintTextAlign,
        content = content,
    )
}

/**
 * A warning view is a component that displays a warning icon, a title, and a hint.
 *
 * @param imageVector The vector icon to be displayed.
 * @param title The title of the warning.
 * @param hint The hint to be displayed, as [AnnotatedString].
 * @param modifier The modifier to be applied to the layout.
 * @param hintTextAlign The text alignment of the hint, defaults to [TextAlign.Center].
 * @param content The content to be displayed below the hint.
 */
@Composable
fun WarningView(
    imageVector: ImageVector,
    title: String,
    hint: AnnotatedString,
    modifier: Modifier = Modifier,
    hintTextAlign: TextAlign? = TextAlign.Center,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    WarningView(
        painterResource = rememberVectorPainter(image = imageVector),
        title = title,
        hint = hint,
        modifier = modifier,
        hintTextAlign = hintTextAlign,
        content = content,
    )
}

/**
 * A warning view is a component that displays a warning icon, a title, and a hint.
 *
 * @param painterResource The painter to be displayed.
 * @param title The title of the warning.
 * @param hint The hint to be displayed, as [AnnotatedString].
 * @param modifier The modifier to be applied to the layout.
 * @param hintTextAlign The text alignment of the hint, defaults to [TextAlign.Center].
 * @param content The content to be displayed below the hint.
 */
@Composable
fun WarningView(
    painterResource: Painter,
    title: String,
    hint: AnnotatedString,
    modifier: Modifier = Modifier,
    hintTextAlign: TextAlign? = TextAlign.Center,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    WarningView(
        painterResource = painterResource,
        title = { Text(text = title) },
        hint = { Hint(text = hint, textAlign = hintTextAlign) },
        modifier = modifier,
        content = content,
    )
}

/**
 * A warning view is a component that displays a warning icon, a title, and a hint.
 *
 * @param painterResource The painter to be displayed.
 * @param title The title of the warning.
 * @param hint The hint to be displayed.
 * @param modifier The modifier to be applied to the layout.
 * @param content The content to be displayed below the hint.
 */
@Composable
fun WarningView(
    painterResource: Painter,
    title: String,
    hint: String,
    modifier: Modifier = Modifier,
    hintTextAlign: TextAlign? = TextAlign.Center,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    WarningView(
        painterResource = painterResource,
        title = { Text(text = title) },
        hint = { Hint(text = hint, textAlign = hintTextAlign) },
        modifier = modifier,
        content = content,
    )
}

/**
 * A warning view is a component that displays a warning icon, a title, and a hint.
 *
 * @param imageVector The vector icon to be displayed.
 * @param title The title of the warning.
 * @param hint The hint to be displayed.
 * @param modifier The modifier to be applied to the layout.
 * @param content The content to be displayed below the hint.
 */
@Composable
fun WarningView(
    imageVector: ImageVector,
    title: @Composable () -> Unit,
    hint: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    WarningView(
        painterResource = rememberVectorPainter(image = imageVector),
        title = title,
        hint = hint,
        modifier = modifier,
        content = content,
    )
}

/**
 * A warning view is a component that displays a warning icon, a title, and a hint.
 *
 * @param painterResource The painter to be displayed.
 * @param title The title of the warning.
 * @param hint The hint to be displayed.
 * @param modifier The modifier to be applied to the layout.
 * @param content The content to be displayed below the hint.
 */
@Composable
fun WarningView(
    painterResource: Painter,
    title: @Composable () -> Unit,
    hint: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BigIcon(painterResource = painterResource)

        title()

        hint()

        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun WarningViewPreview() {
    MaterialTheme {
        WarningView(
            painterResource = painterResource(R.drawable.baseline_warning_24),
            title = "Warning",
            hint = "This is a warning view",
            modifier = Modifier.fillMaxSize(),
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "This is content")
            }
        }
    }
}