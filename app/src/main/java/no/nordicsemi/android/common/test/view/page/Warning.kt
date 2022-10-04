package no.nordicsemi.android.common.test.view.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Paragliding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.PagerViewItem
import no.nordicsemi.android.common.theme.view.WarningView

val Warning = PagerViewItem("Warning") {
    Warning()
}

@Composable
private fun Warning() {
    WarningView(
        imageVector = Icons.Default.Paragliding,
        title = stringResource(id = R.string.warning_title),
        hint = stringResource(id = R.string.warning_text),
        modifier = Modifier.fillMaxSize(),
    ) {
        Button(onClick = { }) {
            Text(text = stringResource(id = R.string.warning_button))
        }
    }
}

@Preview
@Composable
private fun WarningPreview() {
    NordicTheme {
        Warning()
    }
}