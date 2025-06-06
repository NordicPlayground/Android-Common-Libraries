package no.nordicsemi.android.common.scanner.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.scanner.R
import no.nordicsemi.android.common.scanner.data.Filter
import no.nordicsemi.android.common.scanner.data.FilterEvent
import no.nordicsemi.android.common.scanner.data.OnFilterSelected
import no.nordicsemi.android.common.scanner.data.SortBy
import no.nordicsemi.android.common.scanner.data.SortType

@Composable
internal fun SortByView(
    sortByFilters: List<SortBy>,
    activeFilters: List<Filter>,
    onSortOptionSelected: (FilterEvent) -> Unit
) {
    val currentSortByFilter = activeFilters.filterIsInstance<SortBy>()
        .firstOrNull()
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HorizontalDivider()
        Text(text = stringResource(id = R.string.sort_by_title))
        Column(
            modifier = Modifier.selectableGroup()
        ) {
            sortByFilters.forEach { sortBy ->
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .selectable(
                            selected = sortBy.sortType == currentSortByFilter?.sortType,
                            onClick = { onSortOptionSelected(OnFilterSelected(SortBy(sortBy.sortType))) },
                            role = Role.RadioButton
                        ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = sortBy.sortType == currentSortByFilter?.sortType,
                            onClick = { onSortOptionSelected(OnFilterSelected(SortBy(sortBy.sortType))) }
                        )
                        Text(
                            text = sortBy.sortType.toString(),
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
private fun SortByViewPreview() {
    SortByView(
        sortByFilters = listOf(
            SortBy(SortType.RSSI),
            SortBy(SortType.ALPHABETICAL)
        ),
        activeFilters = listOf(),
        onSortOptionSelected = {}
    )
}
