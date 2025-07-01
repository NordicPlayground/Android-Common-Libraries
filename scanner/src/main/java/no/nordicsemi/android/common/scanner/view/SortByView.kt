package no.nordicsemi.android.common.scanner.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
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
import no.nordicsemi.android.common.scanner.ScanFilterState
import no.nordicsemi.android.common.scanner.data.SortingDisabled
import no.nordicsemi.android.common.scanner.data.SortingOption
import no.nordicsemi.android.common.scanner.rememberFilterState

@Composable
internal fun SortByView(
    state: ScanFilterState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SortingOptionRow(state, SortingDisabled)
        state.sortingOptions.forEach { option ->
            SortingOptionRow(state, option)
        }
    }
}

@Composable
private fun SortingOptionRow(
    state: ScanFilterState,
    option: SortingOption
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .selectable(
                selected = option == state.activeSortingOption,
                onClick = { state.sortBy(option) },
                role = Role.RadioButton
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = option == state.activeSortingOption,
                onClick = { state.sortBy(option) }
            )
            Text(
                text = stringResource(option.title)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SortByViewPreview() {
    SortByView(
        state = rememberFilterState(),
    )
}
