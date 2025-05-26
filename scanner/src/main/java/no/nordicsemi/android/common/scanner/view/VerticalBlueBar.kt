package no.nordicsemi.android.common.scanner.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * This composable is used to create a vertical blue bar with a content.
 *
 * @param content The content to be displayed in the blue bar.
 */
@Composable
internal fun VerticalBlueBar(
    content: @Composable ColumnScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(8.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
        )
        Column(
            modifier = Modifier.padding(start = 8.dp),
        ) {
            content()
        }
    }
}
