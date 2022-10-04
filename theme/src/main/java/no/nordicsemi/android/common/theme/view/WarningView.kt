package no.nordicsemi.android.common.theme.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.internal.BigIcon
import no.nordicsemi.android.common.theme.view.internal.Hint
import no.nordicsemi.android.common.theme.view.internal.Title

@Composable
fun WarningView(
    imageVector: ImageVector,
    title: String,
    hint: String,
    modifier: Modifier = Modifier,
    hintTextAlign: TextAlign? = TextAlign.Center,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        BigIcon(imageVector = imageVector)

        Spacer(modifier = Modifier.size(16.dp))

        Title(text = title)

        Spacer(modifier = Modifier.size(16.dp))

        Hint(text = hint, textAlign = hintTextAlign)

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
    hintTextAlign: TextAlign? = TextAlign.Center,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BigIcon(imageVector = imageVector)

        Spacer(modifier = Modifier.size(16.dp))

        Title(text = title)

        Spacer(modifier = Modifier.size(16.dp))

        Hint(text = hint, textAlign = hintTextAlign)

        Spacer(modifier = Modifier.size(16.dp))

        content()
    }
}

@Preview
@Composable
fun WarningViewPreview() {
    NordicTheme {
        WarningView(
            imageVector = Icons.Filled.Warning,
            title = "Warning",
            hint = "This is a warning view",
        )
    }
}