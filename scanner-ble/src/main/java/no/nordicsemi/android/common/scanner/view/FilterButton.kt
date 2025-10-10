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

package no.nordicsemi.android.common.scanner.view

import androidx.annotation.DrawableRes
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import no.nordicsemi.android.common.scanner.R

@Composable
internal fun FilterButton(
    title: String,
    @DrawableRes icon: Int,
    isSelected: Boolean,
    containerColorEnabled: Color = Color.Unspecified,
    containerColorDisabled: Color = Color.Unspecified,
    onClick: () -> Unit = {},
) {
    FilterButton(
        title = title,
        painter = painterResource(icon),
        isSelected = isSelected,
        containerColorEnabled = containerColorEnabled,
        containerColorDisabled = containerColorDisabled,
        onClick = onClick
    )
}

@Composable
internal fun FilterButton(
    title: String,
    painter: Painter,
    isSelected: Boolean,
    containerColorEnabled: Color = Color.Unspecified,
    containerColorDisabled: Color = Color.Unspecified,
    onClick: () -> Unit = {},
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(title) },
        leadingIcon = {
            Icon(
                painter = if (isSelected) painterResource(R.drawable.baseline_close_24) else painter,
                contentDescription = null
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = containerColorDisabled,
            selectedContainerColor = containerColorEnabled,
        ),
    )
}

@Composable
internal fun FilterButton(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    containerColorEnabled: Color = Color.Unspecified,
    containerColorDisabled: Color = Color.Unspecified,
    onClick: () -> Unit = {},
) {
    FilterButton(
        title = title,
        painter = rememberVectorPainter(icon),
        isSelected = isSelected,
        containerColorEnabled = containerColorEnabled,
        containerColorDisabled = containerColorDisabled,
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterButtonPreviewSelected() {
    FilterButton(
        title = "Nearby",
        icon = R.drawable.baseline_my_location_24,
        isSelected = true
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterButtonPreview() {
    FilterButton(
        title = "Named",
        icon = R.drawable.baseline_label_24,
        isSelected = false
    )
}
