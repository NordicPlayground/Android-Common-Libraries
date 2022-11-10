package no.nordicsemi.android.common.test.simple

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import no.nordicsemi.android.common.navigation.createSimpleDestination
import no.nordicsemi.android.common.navigation.defineDestination

val Settings = createSimpleDestination("settings")

val SettingsDestination = defineDestination(Settings) {
    SettingsScreen()
}

@Composable
private fun SettingsScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "This is a simple destination.\nFor example Settings.\nClicking Back button will navigate to the main destination.",
            textAlign = TextAlign.Center,
        )
    }
}