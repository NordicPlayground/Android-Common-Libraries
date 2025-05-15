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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.scanner.data.AllowAddressScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowBondedScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNameScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNearbyScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNonEmptyNameScanResultFilter
import no.nordicsemi.android.common.scanner.data.OnFilterApply
import no.nordicsemi.android.common.scanner.data.OnFilterReset
import no.nordicsemi.android.common.scanner.data.OnFilterSelected
import no.nordicsemi.android.common.scanner.data.ScanResultFilter
import no.nordicsemi.android.common.scanner.data.UiClickEvent
import no.nordicsemi.android.common.scanner.viewmodel.ScanningState
import no.nordicsemi.android.common.theme.nordicGreen
import no.nordicsemi.android.common.ui.view.NordicAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScannerAppBar(
    title: @Composable () -> Unit,
    backButtonIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    showProgress: Boolean = false,
    scanningState: ScanningState,
    filterSelected: List<ScanResultFilter> = emptyList(),
    onFilterSelected: (UiClickEvent) -> Unit,
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
    onFilterSelected: (UiClickEvent) -> Unit,
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
            filterSelected, onDismissRequest
        ) {
            onFilterSelected(it)
        }
    }
}

@Composable
private fun FilterDetails(
    scanningState: ScanningState,
    filterSelected: List<ScanResultFilter> = emptyList(),
    onDismissRequest: () -> Unit,
    onEvent: (UiClickEvent) -> Unit,
) {
    val filterList = filterSelected.toMutableList()

    // list of Peripheral devices with non-empty names
    val displayNamePeripheralList =
        (scanningState as ScanningState.DevicesDiscovered).result.filter { it.peripheral.name != null }
            .distinctBy { it.peripheral.name }
            .sortedBy { it.peripheral.name }
            .map { it.peripheral }


    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Search by",
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    // Add filterSelected to the list and if it is already in the list, remove it.
                    filterList.add(AllowNearbyScanResultFilter)
                    onEvent(OnFilterSelected(AllowNearbyScanResultFilter))
                    println("items inside filterSelected: $filterSelected")
                },
                modifier = Modifier,
                colors = ButtonColors(
                    containerColor = filterList.getButtonContainerColor(
                        AllowNearbyScanResultFilter
                    ),
                    contentColor = filterList.getButtonContentColor(AllowNearbyScanResultFilter),
                    disabledContainerColor = ButtonDefaults.buttonColors().disabledContainerColor,
                    disabledContentColor = ButtonDefaults.buttonColors().disabledContentColor
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                    )
                    Text(
                        "NearBy",
                    )
                }
            }

            Button(
                onClick = {
                    filterList.add(AllowNonEmptyNameScanResultFilter)
                    onEvent(OnFilterSelected(AllowNonEmptyNameScanResultFilter))
                    println("items inside filterSelected: $filterSelected")
                },
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                    )
                    Text(
                        "Names",
                    )
                }
            }

            Button(
                onClick = {
                    filterList.add(AllowBondedScanResultFilter)
                    onEvent(OnFilterSelected(AllowBondedScanResultFilter))
                    println("items inside filterSelected: $filterSelected")
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                    )
                    Text(
                        "Bonded",
                    )
                }
            }
        }

        Text(
            "Filter from the scanned devices",
            modifier = Modifier.padding(start = 8.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var displayNameExpanded by rememberSaveable { mutableStateOf(false) }
            Column {
                Button(
                    onClick = {
                        displayNameExpanded = !displayNameExpanded
                    }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "Display Name",
                        )
                        Icon(
                            imageVector = if (displayNameExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                        )
                    }
                }
                // Display the list of names in the dropdown
                DropdownMenu(
                    expanded = displayNameExpanded,
                    onDismissRequest = { displayNameExpanded = false },
                    modifier = Modifier
                        .padding(16.dp),
                    scrollState = rememberScrollState()
                ) {
                    displayNamePeripheralList.forEach { peripheral ->
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
                                }
                                HorizontalDivider()
                            },
                            onClick = {
                                // Add filterSelected to the list and if it is already in the list, remove it.
                                filterList.add(AllowNameScanResultFilter(peripheral.name!!))
                                filterList.add(AllowAddressScanResultFilter(peripheral.address))
                                onEvent(OnFilterSelected(AllowNameScanResultFilter(peripheral.name!!)))
                                onEvent(OnFilterSelected(AllowAddressScanResultFilter(peripheral.address)))
                                displayNameExpanded = false
                            }
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                onClick = {
                    println("Apply: $filterList")
                    onEvent(OnFilterApply(filterList))
                },
            ) {
                Text(
                    text = "Apply",
                    modifier = Modifier.clickable {
                        onEvent(OnFilterApply(filterList))
                        onDismissRequest()
                    }
                )
            }
            if (filterList.isNotEmpty()) {
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
}
/*
@Preview(showBackground = true)
@Composable
private fun FilterDetailsPreview() {
    FilterDetails(
        scanState = ScanningState.DevicesDiscovered(
            emptyList(), emptyList()
        ),
        onDismissRequest = { },
        onFilterSelected = { }
    )
}*/

/*@Preview
@Composable
private fun ScannerAppBarPreview() {
    ScannerAppBar(
        title = { },
        onNavigationButtonClick = { },
        scanningState = uiState.scanningState
    )
}*/

@Composable
fun <T> MutableList<T>.getButtonContainerColor(item: T): Color {
    println("this: $this")
    return if (contains(item)) nordicGreen else ButtonDefaults.buttonColors().containerColor
}

@Composable
fun <T> MutableList<T>.getButtonContentColor(item: T): Color {
    return if (contains(item)) MaterialTheme.colorScheme.onPrimary else ButtonDefaults.buttonColors().contentColor
}

