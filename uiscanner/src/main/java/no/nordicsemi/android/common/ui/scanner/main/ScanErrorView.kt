package no.nordicsemi.android.common.ui.scanner.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.WarningView
import no.nordicsemi.android.common.ui.scanner.R

@Composable
internal fun ScanErrorView(
    error: Int,
) {
    WarningView(
        modifier = Modifier.padding(16.dp),
        imageVector = Icons.Default.BluetoothSearching,
        title = stringResource(id = R.string.scanner_error),
        hint = stringResource(id = R.string.scan_failed, error),
    )
}

@Preview
@Composable
private fun ErrorSectionPreview() {
    NordicTheme {
        ScanErrorView(3)
    }
}