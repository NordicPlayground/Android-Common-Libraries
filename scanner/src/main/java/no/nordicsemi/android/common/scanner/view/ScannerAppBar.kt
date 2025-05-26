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

package no.nordicsemi.android.common.scanner.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.scanner.data.FilterSettingsState
import no.nordicsemi.android.common.scanner.data.FilterEvent
import no.nordicsemi.android.common.scanner.viewmodel.ScanningState
import no.nordicsemi.android.common.ui.view.NordicAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScannerAppBar(
    title: @Composable () -> Unit,
    showProgress: Boolean = false,
    filterConfig: FilterSettingsState,
    scanningState: ScanningState,
    backButtonIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    onFilterSelected: (FilterEvent) -> Unit,
    onNavigationButtonClick: (() -> Unit)? = null,
) {
    var expandFilterBottomSheet by rememberSaveable { mutableStateOf(false) }

    NordicAppBar(
        title = title,
        backButtonIcon = backButtonIcon,
        onNavigationButtonClick = onNavigationButtonClick,
        actions = {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(30.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                if (filterConfig is FilterSettingsState.Enabled) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                expandFilterBottomSheet = true
                            }
                            .padding(8.dp)
                    )
                }
            }
        },
    )

    if (expandFilterBottomSheet) {
        FilterDialog(
            filterSettings = (filterConfig as FilterSettingsState.Enabled).filter,
            scannedResults = (scanningState as ScanningState.DevicesDiscovered).result,
            activeFilters = scanningState.scanFilter,
            onDismissRequest = { expandFilterBottomSheet = false },
            onFilterSelected = { onFilterSelected(it) }
        )
    }
}
