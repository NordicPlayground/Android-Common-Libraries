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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.scanner.data.AllowBondedScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNearbyScanResultFilter
import no.nordicsemi.android.common.scanner.data.AllowNonEmptyNameScanResultFilter
import no.nordicsemi.android.common.scanner.data.FilterEvent
import no.nordicsemi.android.common.scanner.data.FilterSettings
import no.nordicsemi.android.common.scanner.data.GroupByName
import no.nordicsemi.android.common.scanner.data.OnFilterReset
import no.nordicsemi.android.common.scanner.data.OnFilterSelected
import no.nordicsemi.android.common.scanner.data.ScanResultFilter
import no.nordicsemi.android.common.scanner.data.SortScanResult
import no.nordicsemi.android.common.scanner.data.SortType
import no.nordicsemi.android.common.scanner.data.toDisplayTitle
import no.nordicsemi.kotlin.ble.client.android.ScanResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FilterDialog(
    filterSettings: FilterSettings,
    scannedResults: List<ScanResult>,
    activeFilters: List<ScanResultFilter>,
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
            filterSettings = filterSettings,
            scannedResults = scannedResults,
            activeFilters
        ) {
            onFilterSelected(it)
        }
    }
}

@Composable
private fun FilterContent(
    filterSettings: FilterSettings,
    scannedResults: List<ScanResult>,
    activeFilters: List<ScanResultFilter>,
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
            if (filterSettings.showNearby)
                FilterButton(
                    filter = AllowNearbyScanResultFilter,
                    isSelected = activeFilters.contains(AllowNearbyScanResultFilter),
                    onClick = { onFilterSelected(OnFilterSelected(AllowNearbyScanResultFilter)) }
                )
            if (filterSettings.showNonEmptyName)
                FilterButton(
                    filter = AllowNonEmptyNameScanResultFilter,
                    isSelected = activeFilters.contains(AllowNonEmptyNameScanResultFilter),
                    onClick = { onFilterSelected(OnFilterSelected(AllowNonEmptyNameScanResultFilter)) }
                )
            if (filterSettings.showBonded)
                FilterButton(
                    filter = AllowBondedScanResultFilter,
                    isSelected = activeFilters.contains(AllowBondedScanResultFilter),
                    onClick = { onFilterSelected(OnFilterSelected(AllowBondedScanResultFilter)) }
                )
        }

        if (filterSettings.showSortByOption) {
            SortByFilterView(
                activeFilters = activeFilters,
                onSortOptionSelected = onFilterSelected,
            )
        }

        if (filterSettings.showGroupByDropdown && scannedResults.isNotEmpty()) {
            GroupByNameDropdown(
                dropdownLabel = dropdownLabel,
                onLabelChange = { dropdownLabel = it },
                scanResults = scannedResults,
                onItemSelected = { onFilterSelected(it) },
            )
        }
    }
}

@Composable
private fun GroupByNameDropdown(
    title: String = "Group by display name",
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
                    Text(dropdownLabel.takeIf { it.isNotEmpty() } ?: "Select item")
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
                groupedResults.forEach { (name, items) ->
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
                                onItemSelected(
                                    OnFilterSelected(GroupByName(name, items))
                                )
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
            "Filter results",
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
                        text = "Clear all",
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
private fun FilterButtonPreview() {
    FilterButton(
        filter = AllowNearbyScanResultFilter,
        isSelected = true,
        onClick = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterDetailsPreview() {
    FilterContent(
        filterSettings = FilterSettings(
            showNonEmptyName = true,
            showBonded = true,
            showNearby = true,
            showGroupByDropdown = true
        ),
        scannedResults = emptyList(),
        activeFilters = emptyList(),
        onFilterSelected = {}
    )
}

@Composable
private fun FilterButton(
    filter: ScanResultFilter,
    isSelected: Boolean,
    containerColorEnabled: Color = ButtonDefaults.buttonColors().containerColor,
    containerColorDisabled: Color = ButtonDefaults.buttonColors().disabledContainerColor,
    onClick: () -> Unit,
) {
    val containerColor = if (isSelected)
        containerColorEnabled else containerColorDisabled

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
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
    activeFilters: List<ScanResultFilter>,
    onSortOptionSelected: (FilterEvent) -> Unit
) {
    val currentSortByFilter = activeFilters.filterIsInstance<SortScanResult>()
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
            SortType.entries.forEach { sortType ->
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .selectable(
                            selected = sortType == currentSortByFilter?.sortType,
                            onClick = {
                                // onClick event.
                                onSortOptionSelected(OnFilterSelected(SortScanResult(sortType)))
                            },
                            role = Role.RadioButton
                        ),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = sortType == currentSortByFilter?.sortType,
                            onClick = null
                        )
                        Text(
                            text = sortType.toString(),
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
    SortByFilterView(
        activeFilters = listOf(SortScanResult(SortType.RSSI)),
        onSortOptionSelected = { }
    )
}
