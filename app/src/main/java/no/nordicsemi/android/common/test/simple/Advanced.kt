package no.nordicsemi.android.common.test.simple

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.navigation.createSimpleDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.theme.NordicTheme

val Advanced = createSimpleDestination("advanced")

val AdvancedDestination = defineDestination(Advanced) {
    AdvancedScreen(
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun AdvancedScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "This is a simple destination with some advanced settings.\nClicking Back button will navigate to the main destination.",
                textAlign = TextAlign.Center,
            )

            Button(onClick = { }) {
                Text(text = stringResource(id = R.string.action_no_op))
            }
        }
    }
}

@Preview
@Composable
private fun SimpleContentPreview() {
    NordicTheme {
        AdvancedScreen()
    }
}