package no.nordicsemi.android.common.test.view.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Paragliding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Button(onClick = { }) {
                Text(text = stringResource(id = R.string.warning_normal))
            }

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                )
            ) {
                Text(
                    text = stringResource(id = R.string.warning_dangerous),
                )
            }
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