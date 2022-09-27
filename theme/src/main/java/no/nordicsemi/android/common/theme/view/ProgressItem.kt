package no.nordicsemi.android.common.theme.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.theme.R

enum class ProgressItemStatus {
    DISABLED, WORKING, SUCCESS, ERROR
}

@Composable
fun ProgressItem(
    text: String,
    status: ProgressItemStatus,
    modifier: Modifier = Modifier,
    iconRightPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(status.toImageRes()),
            contentDescription = null,
            tint = status.toIconColor(),
        )
        Spacer(modifier = Modifier.width(iconRightPadding))
        Column {
            ProvideTextStyle(value = MaterialTheme.typography.bodyMedium) {
                Text(
                    text = text,
                    color = status.toTextColor(),
                )
                content()
            }
        }
    }
}

@Composable
private fun ProgressItemStatus.toIconColor(): Color {
    return when (this) {
        ProgressItemStatus.DISABLED -> MaterialTheme.colorScheme.surfaceVariant
        ProgressItemStatus.WORKING -> LocalContentColor.current
        ProgressItemStatus.SUCCESS -> colorResource(id = R.color.nordicGrass)
        ProgressItemStatus.ERROR -> MaterialTheme.colorScheme.error
    }
}

@Composable
private fun ProgressItemStatus.toTextColor(): Color {
    return when (this) {
        ProgressItemStatus.DISABLED -> LocalContentColor.current.copy(alpha = 0.38f)
        ProgressItemStatus.WORKING,
        ProgressItemStatus.SUCCESS,
        ProgressItemStatus.ERROR -> LocalContentColor.current
    }
}

@DrawableRes
private fun ProgressItemStatus.toImageRes(): Int {
    return when (this) {
        ProgressItemStatus.DISABLED -> R.drawable.ic_dot
        ProgressItemStatus.WORKING -> R.drawable.ic_arrow_right
        ProgressItemStatus.SUCCESS -> R.drawable.ic_check
        ProgressItemStatus.ERROR -> R.drawable.ic_cross
    }
}

@Preview
@Composable
fun ProgressItemPreview() {
    MaterialTheme {
        ProgressItem(
            text = "Uploading",
            status = ProgressItemStatus.WORKING,
        ) {
            LinearProgressIndicator(
                progress = 0.3f,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "30%",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}