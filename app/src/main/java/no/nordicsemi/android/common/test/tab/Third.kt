package no.nordicsemi.android.common.test.tab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import no.nordicsemi.android.common.navigation.createSimpleDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.theme.NordicTheme

val ThirdTab = createSimpleDestination("third")

val ThirdDestination = defineDestination(ThirdTab) {
    ThirdScreen()
}

@Composable
private fun ThirdScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "This tab is not using inner navigation.\nThe content is just a composable.",
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun ThirdScreenPreview() {
    NordicTheme {
        ThirdScreen()
    }
}