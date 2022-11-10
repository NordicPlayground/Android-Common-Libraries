package no.nordicsemi.android.common.theme.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NavigationDrawerTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.height(56.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

object NavigationDrawerTitleDefaults {
    /**
     * Default external padding for a [NavigationDrawerTitle] according to the Material
     * specification.
     */
    val ItemPadding = PaddingValues(horizontal = 28.dp)
}

object NavigationDrawerDividerDefaults {
    /**
     * Default external padding for a divider in Navigation Drawer according to the Material
     * specification.
     */
    val ItemPadding = PaddingValues(horizontal = 28.dp)
}