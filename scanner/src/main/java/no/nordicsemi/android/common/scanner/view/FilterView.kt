package no.nordicsemi.android.common.scanner.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.scanner.R
import no.nordicsemi.android.common.scanner.data.Filter
import no.nordicsemi.android.common.scanner.data.FilterEvent
import no.nordicsemi.android.common.scanner.data.GroupByName
import no.nordicsemi.android.common.scanner.data.OnFilterReset
import no.nordicsemi.android.common.scanner.data.OnFilterSelected
import no.nordicsemi.android.common.scanner.data.OnlyBonded
import no.nordicsemi.android.common.scanner.data.OnlyNearby
import no.nordicsemi.android.common.scanner.data.OnlyWithNames
import no.nordicsemi.android.common.scanner.data.SortBy
import no.nordicsemi.android.common.scanner.data.SortType
import no.nordicsemi.kotlin.ble.client.android.ScanResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FilterDialog(
    availableFilters: List<Filter>,
    scannedResults: List<ScanResult>,
    activeFilters: List<Filter>,
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
        FilterContent(
            availableFilters = availableFilters,
            scannedResults = scannedResults,
            activeFilters
        ) {
            onFilterSelected(it)
        }
    }
}

@Composable
private fun FilterContent(
    availableFilters: List<Filter>,
    scannedResults: List<ScanResult>,
    activeFilters: List<Filter>,
    onFilterSelected: (FilterEvent) -> Unit,
) {
    var dropdownLabel by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterTopView(
            isSelectedFilterEmpty = activeFilters.isEmpty(),
            onFilterReset = {
                dropdownLabel = ""
                onFilterSelected(it)
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableFilters.any { it::class == OnlyNearby::class }.let { isAvailable ->
                if (isAvailable) {
                    OnlyNearby().Draw(
                        isSelected = activeFilters.any { it::class == OnlyNearby::class }
                    ) {
                        onFilterSelected(OnFilterSelected(OnlyNearby()))
                    }
                }
            }
            availableFilters.any { it::class == OnlyWithNames::class }.let { isAvailable ->
                if (isAvailable) {
                    OnlyWithNames().draw(
                        isSelected = activeFilters.any { it::class == OnlyWithNames::class }
                    ) {
                        onFilterSelected(OnFilterSelected(OnlyWithNames()))
                    }
                }
            }
            availableFilters.any { it::class == OnlyBonded::class }.let { isAvailable ->
                if (isAvailable) {
                    OnlyBonded().Draw(
                        isSelected = activeFilters.any { it::class == OnlyBonded::class }
                    ) {
                        onFilterSelected(OnFilterSelected(OnlyBonded()))
                    }
                }
            }
        }

        availableFilters.any { it::class == SortBy::class }.let { isAvailable ->
            if (isAvailable) {
                SortBy().Draw(
                    activeFilters = activeFilters,
                    onSortOptionSelected = onFilterSelected,
                )
            }
        }
        availableFilters.any { it::class == GroupByName::class }.let { isAvailable ->
            if (isAvailable && scannedResults.isNotEmpty()) {
                GroupByName(dropdownLabel).Draw(
                    dropdownLabel = dropdownLabel,
                    onLabelChange = { dropdownLabel = it },
                    scanResults = scannedResults,
                    onItemSelected = { onFilterSelected(it) },
                )
            }
        }
    }
}

@Composable
internal fun GroupByNameDropdown(
    title: String = stringResource(id = R.string.group_by_title),
    dropdownLabel: String,
    onLabelChange: (String) -> Unit,
    scanResults: List<ScanResult>,
    onItemSelected: (FilterEvent) -> Unit,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Text(title)
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column {
            Button(onClick = { isExpanded = !isExpanded }) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(dropdownLabel.takeIf { it.isNotEmpty() }
                        ?: stringResource(id = R.string.empty_dropdown_label))
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
            ) {
                val groupedResults = scanResults.groupBy { it.peripheral.name }
                groupedResults.forEach { (name, _) ->
                    name?.let {
                        DropdownMenuItem(
                            text = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalAlignment = Alignment.Start,

                                    ) {
                                    Text(name)
                                    // show horizontal divider if there are multiple items
                                    if (groupedResults.values.size > 1) {
                                        HorizontalDivider()
                                    }
                                }
                            },
                            onClick = {
                                onItemSelected(OnFilterSelected(GroupByName(name)))
                                onLabelChange(name)
                                isExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterTopView(
    isSelectedFilterEmpty: Boolean,
    containerColor: Color = MaterialTheme.colorScheme.tertiary,
    contentColor: Color = MaterialTheme.colorScheme.onTertiary,
    onFilterReset: (FilterEvent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.filter_title),
            modifier = Modifier
                .weight(1f)
        )
        if (!isSelectedFilterEmpty) {
            TextButton(
                onClick = {
                    onFilterReset(OnFilterReset)
                },

                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )

                    Text(
                        text = stringResource(id = R.string.clear_all),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterTopViewPreview() {
    FilterTopView(
        isSelectedFilterEmpty = false,
        onFilterReset = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterDetailsPreview() {
    FilterContent(
        availableFilters = listOf(
            OnlyNearby(),
            OnlyWithNames(),
            OnlyBonded(),
            SortBy(SortType.RSSI),
            GroupByName(""),
        ),
        scannedResults = emptyList(),
        activeFilters = emptyList(),
        onFilterSelected = {}
    )
}
