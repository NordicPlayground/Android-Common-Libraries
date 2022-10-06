package no.nordicsemi.android.common.test.view.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.PagerViewItem

val Fonts = PagerViewItem("Fonts") {
    Fonts()
}

@Composable
private fun Fonts() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Text samples
        Text(
            text = stringResource(id = R.string.text_display_large),
            style = MaterialTheme.typography.displayLarge,
        )
        Text(
            text = stringResource(id = R.string.text_display_medium),
            style = MaterialTheme.typography.displayMedium,
        )
        Text(
            text = stringResource(id = R.string.text_display_small),
            style = MaterialTheme.typography.displaySmall,
        )
        Text(
            text = stringResource(id = R.string.text_title_large),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(id = R.string.text_title_medium),
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = stringResource(id = R.string.text_title_small),
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            text = stringResource(id = R.string.text_body_large),
            // style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = stringResource(id = R.string.text_body_medium),
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = stringResource(id = R.string.text_body_small),
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = stringResource(id = R.string.text_label_large),
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = stringResource(id = R.string.text_label_medium),
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = stringResource(id = R.string.text_label_small),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
private fun FontsPreview() {
    NordicTheme {
        Fonts()
    }
}