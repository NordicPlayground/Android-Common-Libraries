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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.scanner.R
import no.nordicsemi.android.common.scanner.ScanFilterState
import no.nordicsemi.android.common.scanner.rememberFilterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    state: ScanFilterState,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 16.dp,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .width(50.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    ) {
        FilterContent(state = state, onDismissRequest = onDismissRequest)
    }
}

@Composable
private fun FilterContent(
    state: ScanFilterState,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Header(
            text = if (state.dynamicFilters.isNotEmpty())
                stringResource(id = R.string.filter_title)
            else
                stringResource(id = R.string.sort_by_title),
            modifier = Modifier.padding(horizontal = 16.dp),
            isSelectedFilterEmpty = state.isEmpty,
            onFilterReset = {
                state.clear()
                onDismissRequest()
            }
        )
        if (state.dynamicFilters.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                state.dynamicFilters.forEachIndexed { index, filter ->
                    FilterButton(
                        title = stringResource(id = filter.title),
                        icon = filter.icon,
                        isSelected = state.isFilterSelected(index),
                        onClick = { state.toggleFilter(index) }
                    )
                }
            }
        }

        if (state.dynamicFilters.isNotEmpty() && state.sortingOptions.isNotEmpty()) {
            HorizontalDivider()

            Text(
                text = stringResource(id = R.string.sort_by_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp)
            )
        }

        if (state.sortingOptions.isNotEmpty()) {
            SortByView(
                state = state,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun Header(
    text: String,
    isSelectedFilterEmpty: Boolean,
    modifier: Modifier = Modifier,
    onFilterReset: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(1f)
        )
        TextButton(
            onClick = onFilterReset,
            enabled = !isSelectedFilterEmpty,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_delete_24),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(id = R.string.clear_all),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterDetailsPreview() {
    FilterContent(
        state = rememberFilterState(),
        onDismissRequest = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterDetailsPreview_empty() {
    FilterContent(
        state = rememberFilterState(
            dynamicFilters = emptyList()
        ),
        onDismissRequest = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterDetailsPreview_noSorting() {
    FilterContent(
        state = rememberFilterState(
            sortingOptions = emptyList()
        ),
        onDismissRequest = {}
    )
}
