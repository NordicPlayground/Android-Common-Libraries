package no.nordicsemi.android.common.theme.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.theme.view.internal.BigIcon
import no.nordicsemi.android.common.theme.view.internal.Hint
import no.nordicsemi.android.common.theme.view.internal.Title

@Composable
fun WarningView(
    imageVector: ImageVector,
    title: String,
    hint: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp),
    ) {
        BigIcon(imageVector = imageVector)

        Spacer(modifier = Modifier.size(16.dp))

        Title(text = title)

        Spacer(modifier = Modifier.size(16.dp))

        Hint(text = hint)

        Spacer(modifier = Modifier.size(16.dp))

        content()
    }
}

@Composable
fun WarningView(
    imageVector: ImageVector,
    title: String,
    hint: AnnotatedString,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp),
    ) {
        BigIcon(imageVector = imageVector)

        Spacer(modifier = Modifier.size(16.dp))

        Title(text = title)

        Spacer(modifier = Modifier.size(16.dp))

        Hint(text = hint)

        Spacer(modifier = Modifier.size(16.dp))

        content()
    }
}