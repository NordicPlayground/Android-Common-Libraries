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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.scanner.data.AllowBondedScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNameAndAddressScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNearbyScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNonEmptyNameScanResultFilter
import no.nordicsemi.android.common.scanner.data.OnFilterReset
import no.nordicsemi.android.common.scanner.data.OnFilterSelected
import no.nordicsemi.android.common.scanner.data.OnSortBySelected
import no.nordicsemi.android.common.scanner.data.ScanResultFilter
import no.nordicsemi.android.common.scanner.data.SortScanResult
import no.nordicsemi.android.common.scanner.data.SortType
import no.nordicsemi.android.common.scanner.data.UiClickEvent
import no.nordicsemi.android.common.scanner.data.toDisplayTitle
import no.nordicsemi.android.common.scanner.viewmodel.ScanningState
import no.nordicsemi.android.common.theme.nordicGreen
import no.nordicsemi.android.common.ui.view.NordicAppBar
import no.nordicsemi.kotlin.ble.client.android.Peripheral

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScannerAppBar(
    title: @Composable () -> Unit,
    backButtonIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    showProgress: Boolean = false,
    scanningState: ScanningState,
    filterSelected: List<ScanResultFilter> = emptyList(),
    onFilterSelected: (FilterEvent) -> Unit,
    onNavigationButtonClick: (() -> Unit)? = null,
) {
    var showFilterSettings by rememberSaveable { mutableStateOf(false) }
    NordicAppBar(
        title = title,
        backButtonIcon = backButtonIcon,
        onNavigationButtonClick = onNavigationButtonClick,
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                if (showProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(30.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            showFilterSettings = true
                        }
                )
            }
        },
    )

    if (showFilterSettings) {
        FilterDialog(
            scanningState = scanningState,
            filterSelected = filterSelected,
            onDismissRequest = { showFilterSettings = false },
            onFilterSelected = {
                onFilterSelected(it)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FilterDialog(
    scanningState: ScanningState,
    filterSelected: List<ScanResultFilter> = emptyList(),
    onDismissRequest: () -> Unit,
    onFilterSelected: (FilterEvent) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
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
        FilterDetails(
            scanningState,
            filterSelected
        ) {
            onFilterSelected(it)
        }
    }
}

@Composable
private fun FilterDetails(
    scanningState: ScanningState,
    filterSelected: List<ScanResultFilter> = emptyList(),
    onEvent: (FilterEvent) -> Unit,
) {
    val filterList: List<ScanResultFilter> = filterSelected

    // list of Peripheral devices with non-empty names
    val displayNamePeripheralList = remember(scanningState) {
        (scanningState as ScanningState.DevicesDiscovered).result
            .filter { it.peripheral.name != null }
            .distinctBy { it.peripheral.name }
            .sortedBy { it.peripheral.name }
            .map { it.peripheral }
    }

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterTopView(filterSelected.isNotEmpty()) {
            onEvent(it)
        }

        PreviousFilterOptions(filterSelected, onEvent)

        SortByFilterView(
            filterSelected = filterSelected,
            onEvent = onEvent,
        )

        DisplayNameDropDown(
            items = displayNamePeripheralList,
            onItemSelected = { onEvent(it) },
        )
    }
}

@Composable
private fun PreviousFilterOptions(
    filterList: List<ScanResultFilter>,
    onEvent: (FilterEvent) -> Unit,
    isNearbyScanResultFilter: Boolean = true,
    isNonEmptyNameScanResultFilter: Boolean = true,
    isBondedScanResultFilter: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (isNearbyScanResultFilter)
            FilterButton(
                filter = AllowNearbyScanResultFilter,
                isSelected = filterList.contains(AllowNearbyScanResultFilter),
                onClick = { onEvent(OnFilterSelected(AllowNearbyScanResultFilter)) }
            )
        if (isNonEmptyNameScanResultFilter)
            FilterButton(
                filter = AllowNonEmptyNameScanResultFilter,
                isSelected = filterList.contains(AllowNonEmptyNameScanResultFilter),
                onClick = { onEvent(OnFilterSelected(AllowNonEmptyNameScanResultFilter)) }
            )
        if (isBondedScanResultFilter)
            FilterButton(
                filter = AllowBondedScanResultFilter,
                isSelected = filterList.contains(AllowBondedScanResultFilter),
                onClick = { onEvent(OnFilterSelected(AllowBondedScanResultFilter)) }
            )
    }
}

@Composable
private fun DisplayNameDropDown(
    items: List<Peripheral>,
    onItemSelected: (FilterEvent) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var dropdownLabel by rememberSaveable { mutableStateOf("") }

    Text("Display Name")
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column {
            Button(onClick = { expanded = !expanded }) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(dropdownLabel.takeIf { it.isNotEmpty() } ?: "Select item")
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.padding(16.dp)
            ) {
                items.forEach { peripheral ->
                    DropdownMenuItem(
                        text = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.Start,

                                ) {
                                Text(peripheral.name!!)
                                Text(
                                    text = peripheral.address,
                                    style = MaterialTheme.typography.bodySmall
                                        .copy(color = MaterialTheme.colorScheme.onSurface)
                                )
                                // show horizontal divider unless it's the last item
                                if (peripheral != items.last()) {
                                    HorizontalDivider()
                                }
                            }

                        },
                        onClick = {
                            onItemSelected(
                                OnFilterSelected(
                                    AllowNameAndAddressScanResultFilter(
                                        peripheral.name!!,
                                        peripheral.address
                                    )
                                )
                            )
                            dropdownLabel = peripheral.name!!
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterTopView(
    isSelectedFilterNotEmpty: Boolean,
    onEvent: (UiClickEvent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "Filter scan results",
            modifier = Modifier
                .weight(1f)
        )
        if (isSelectedFilterNotEmpty) {
            Button(
                onClick = {
                    onEvent(OnFilterReset)
                },

                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(
                    text = "Clear All",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterButtonPreview() {
    FilterButton(
        filter = AllowNearbyScanResultFilter,
        isSelected = true,
        onClick = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterTopViewPreview() {
    FilterTopView(
        isSelectedFilterNotEmpty = true,
        onEvent = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterDialogPreview() {
    FilterDialog(
        scanningState = ScanningState.DevicesDiscovered(
            emptyList()
        ),
        filterSelected = emptyList(),
        onDismissRequest = { },
        onFilterSelected = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterDetailsPreview() {
    FilterDetails(
        scanningState = ScanningState.DevicesDiscovered(
            emptyList()
        ),
        filterSelected = emptyList(),
        onEvent = {}
    )
}

@Composable
private fun FilterButton(
    filter: ScanResultFilter,
    isSelected: Boolean,
    containerColor: Color = nordicGreen,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) containerColor else ButtonDefaults.buttonColors().containerColor,
            contentColor = if (isSelected) contentColor else ButtonDefaults.buttonColors().contentColor
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (isSelected) Icons.Default.Close else Icons.Default.Done,
                contentDescription = null,
            )
            Text(filter.toDisplayTitle())
        }
    }
}

@Composable
private fun SortByFilterView(
    filterSelected: List<ScanResultFilter> = emptyList(),
    onEvent: (FilterEvent) -> Unit
) {
    val currentSortByFilter = filterSelected.filterIsInstance<SortScanResult>()
        .firstOrNull()
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HorizontalDivider()
        Text("Sort by")
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.selectableGroup()
        ) {
            SortType.entries.forEach { text ->
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .selectable(
                            selected = text == currentSortByFilter?.sortType,
                            onClick = {
                                // onClick event.
                                onEvent(OnSortBySelected(text))
                            },
                            role = Role.RadioButton
                        ),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = text == currentSortByFilter?.sortType,
                            onClick = null
                        )
                        Text(
                            text = text.toString(),
                        )

                    }
                }
            }
        }
        HorizontalDivider()
    }

}

@Preview(showBackground = true)
@Composable
private fun SortByFilterViewPreview() {
    SortByFilterView {}
}


