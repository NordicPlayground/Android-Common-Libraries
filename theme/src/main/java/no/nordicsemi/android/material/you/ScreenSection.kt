package no.nordicsemi.android.material.you

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenSection(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
    ) {
        val columnModifier = if (onClick != null) {
            modifier
                .clickable { onClick.invoke() }
                .fillMaxWidth()
                .padding(16.dp)
        } else {
            modifier
                .fillMaxWidth()
                .padding(16.dp)
        }

        Column(
            modifier = columnModifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}
