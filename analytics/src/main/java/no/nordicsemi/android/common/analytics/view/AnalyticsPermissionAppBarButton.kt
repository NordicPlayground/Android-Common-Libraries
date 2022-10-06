package no.nordicsemi.android.common.analytics.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import no.nordicsemi.android.common.analytics.R
import no.nordicsemi.android.common.theme.view.AppBarIcon

@Composable
fun AnalyticsPermissionButton() {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    AppBarIcon(
        painter = painterResource(id = R.drawable.ic_firebase),
        contentDescription = stringResource(id = R.string.analytics_permission_button),
        onClick = { showDialog = true }
    )

    if (showDialog) {
        AnalyticsPermissionSwitchDialog(
            onDismiss = { showDialog = false }
        )
    }
}

@Preview
@Composable
private fun AnalyticsPermissionButtonPreview() {
    AnalyticsPermissionButton()
}