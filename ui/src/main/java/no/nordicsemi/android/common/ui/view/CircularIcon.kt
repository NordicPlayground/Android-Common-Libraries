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

@file:Suppress("unused")

package no.nordicsemi.android.common.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.ui.R

/**
 * A circular icon with a background.
 *
 * @param painter The painter to be used for the icon.
 * @param modifier The modifier to be applied to the icon.
 * @param backgroundColor The background color of the circle.
 * @param enabled If false, the icon will be displayed in a disabled state.
 * @param size The size of the circular background.
 * @param iconSize The size of the icon inside the circle.
 */
@Composable
fun CircularIcon(
    painter: Painter,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    enabled: Boolean = true,
    size: Dp = 40.dp,
    iconSize: Dp = 24.dp,
) {
    val padding = (size - iconSize) / 2
    Image(
        painter = painter,
        contentDescription = null,
        colorFilter = if (enabled) {
            ColorFilter.tint(MaterialTheme.colorScheme.contentColorFor(backgroundColor))
        } else {
            ColorFilter.tint(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f))
        },
        modifier = Modifier
            .size(size)
            .then(modifier)
            .background(
                color = if (enabled) {
                    backgroundColor
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                },
                shape = CircleShape
            )
            .padding(padding)
    )
}

/**
 * A circular icon with a background.
 *
 * @param imageVector The vector image to be used for the icon.
 * @param modifier The modifier to be applied to the icon.
 * @param backgroundColor The background color of the circle.
 * @param enabled If false, the icon will be displayed in a disabled state.
 * @param size The size of the circular background.
 * @param iconSize The size of the icon inside the circle.
 */
@Composable
fun CircularIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    enabled: Boolean = true,
    size: Dp = 40.dp,
    iconSize: Dp = 24.dp,
) {
    CircularIcon(
        painter = rememberVectorPainter(image = imageVector),
        modifier = modifier,
        backgroundColor = backgroundColor,
        enabled = enabled,
        iconSize = iconSize,
    )
}

@Preview(showBackground = true)
@Composable
private fun CircularIconPreview() {
    MaterialTheme {
        CircularIcon(
            painter = painterResource(R.drawable.ic_check)
        )
    }
}