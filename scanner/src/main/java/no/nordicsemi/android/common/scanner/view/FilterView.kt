package no.nordicsemi.android.common.scanner.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.scanner.R
import no.nordicsemi.android.common.scanner.ScanFilterState
import no.nordicsemi.android.common.scanner.rememberFilterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FilterDialog(
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
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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

//@Composable
//internal fun GroupByNameDropdown(
//    title: String = stringResource(id = R.string.group_by_title),
//    dropdownLabel: String,
//    onLabelChange: (String) -> Unit,
//    scanResults: List<ScanResult>,
//    onItemSelected: (FilterEvent) -> Unit,
//) {
//    var isExpanded by rememberSaveable { mutableStateOf(false) }
//
//    Text(title)
//    Row(
//        modifier = Modifier
//            .fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Column {
//            Button(onClick = { isExpanded = !isExpanded }) {
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(4.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(dropdownLabel.takeIf { it.isNotEmpty() }
//                        ?: stringResource(id = R.string.empty_dropdown_label))
//                    Icon(
//                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
//                        contentDescription = null
//                    )
//                }
//            }
//            DropdownMenu(
//                expanded = isExpanded,
//                onDismissRequest = { isExpanded = false },
//            ) {
//                val groupedResults = scanResults.groupBy { it.peripheral.name }
//                groupedResults.forEach { (name, _) ->
//                    name?.let {
//                        DropdownMenuItem(
//                            text = {
//                                Column(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(8.dp),
//                                    verticalArrangement = Arrangement.spacedBy(4.dp),
//                                    horizontalAlignment = Alignment.Start,
//
//                                    ) {
//                                    Text(name)
//                                    // show horizontal divider if there are multiple items
//                                    if (groupedResults.values.size > 1) {
//                                        HorizontalDivider()
//                                    }
//                                }
//                            },
//                            onClick = {
//                                onItemSelected(OnFilterSelected(GroupByName(name)))
//                                onLabelChange(name)
//                                isExpanded = false
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

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
