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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.scanner.data.AllowAddressScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowBondedScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNameScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNearbyScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNonEmptyNameScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowUuidScanResultFilter
import no.nordicsemi.android.common.scanner.data.ScanResultFilter
import no.nordicsemi.android.common.ui.view.NordicAppBar
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toKotlinUuid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScannerAppBar(
    title: @Composable () -> Unit,
    showProgress: Boolean = false,
    backButtonIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    onFilterSelected: (ScanResultFilter) -> Unit = { },
    onNavigationButtonClick: (() -> Unit)? = null,
) {
    var openFilterDialog by rememberSaveable { mutableStateOf(false) }
    NordicAppBar(
        title = title,
        backButtonIcon = backButtonIcon,
        onNavigationButtonClick = onNavigationButtonClick,
        actions = {
            Row {
                if (showProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .size(30.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            openFilterDialog = true
                        }
                        .padding(4.dp)
                )
            }
        },
    )

    if (openFilterDialog) {
        FilterDialog(
            onDismissRequest = { openFilterDialog = false },
            onFilterSelected = {
                onFilterSelected(it)
                openFilterDialog = false
            }
        )
    }
}

val CHANNEL_SOUND_SERVICE_UUID: UUID = UUID.fromString("0000185B-0000-1000-8000-00805F9B34FB")

@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    onDismissRequest: () -> Unit,
    onFilterSelected: (ScanResultFilter) -> Unit
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
        FilterDetails(onDismissRequest) {
            onFilterSelected(it)
        }
    }
}

@Composable
@OptIn(ExperimentalUuidApi::class)
private fun FilterDetails(
    onDismissRequest: () -> Unit,
    onFilterSelected: (ScanResultFilter) -> Unit,

    ) {
    val filterSelected: MutableState<List<ScanResultFilter>> =
        remember { mutableStateOf(emptyList()) }
    val isSelected: (ScanResultFilter) -> Boolean = { filter ->
        filterSelected.value.contains(filter)
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row {
            Button(
                onClick = {
                    // Add filterSelected to the list.
                    filterSelected.value += AllowNearbyScanResultFilter
                },
                modifier = Modifier
                    .background(
                        color = if (isSelected(AllowNearbyScanResultFilter)) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(8.dp)
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
                        modifier = Modifier.clickable {
                            onFilterSelected(AllowNearbyScanResultFilter)
                            onDismissRequest()
                        }
                    )
                }
            }

            Button(
                onClick = {
                    // Add filterSelected to the list.
                    filterSelected.value += AllowNonEmptyNameScanResultFilter
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
                        "Non Empty Name",
                        modifier = Modifier.clickable {
                            onFilterSelected(AllowNonEmptyNameScanResultFilter)
                            onDismissRequest()
                        }
                    )
                }
            }
        }

        Text(
            "Search by",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        val textFieldValue = remember { mutableStateOf("Name") }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = textFieldValue.value,
                onValueChange = {
                    textFieldValue.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                label = { Text("Display name") }
            )
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        if (textFieldValue.value.trim().isNotEmpty()) {
                            onFilterSelected(AllowNameScanResultFilter(textFieldValue.value))
                        }
                    }
                    .padding(8.dp)
            )
        }

        val deviceAddress = remember { mutableStateOf("Device Address") }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = deviceAddress.value,
                onValueChange = {
                    deviceAddress.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                label = { Text("Device Address") }
            )
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        if (textFieldValue.value.trim().isNotEmpty()) {
                            onFilterSelected(AllowNameScanResultFilter(textFieldValue.value))
                        }
                    }
                    .padding(8.dp)
            )
        }




        Text(
            "Name",
            modifier = Modifier.clickable {
                onFilterSelected(AllowNameScanResultFilter("Nordic CS Reflector 123"))
                onDismissRequest()
            })
        Text(
            "Bonded",
            modifier = Modifier.clickable {
                onFilterSelected(AllowBondedScanResultFilter)
                onDismissRequest()
            })
        Text(
            "UUID",
            modifier = Modifier.clickable {
                onFilterSelected(AllowUuidScanResultFilter(CHANNEL_SOUND_SERVICE_UUID.toKotlinUuid()))
                onDismissRequest()
            })
        Text(
            "Address",
            modifier = Modifier.clickable {
                onFilterSelected(
                    AllowAddressScanResultFilter(
                        "D4:09:B6:97:D1:EB"
                    )
                )
                onDismissRequest()
            })
        ElevatedButton(
            onClick = { onFilterSelected(filterSelected.value.first()) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Apply",
                modifier = Modifier.clickable {
                    onFilterSelected(filterSelected.value.first())
                    onDismissRequest()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterDetailsPreview() {
    FilterDetails(
        onDismissRequest = { },
        onFilterSelected = { }
    )
}

@Preview
@Composable
private fun ScannerAppBarPreview() {
    ScannerAppBar(
        title = { },
        showProgress = false,
        onNavigationButtonClick = { }
    )
}