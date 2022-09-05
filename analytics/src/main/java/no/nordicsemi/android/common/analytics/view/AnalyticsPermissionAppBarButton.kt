package no.nordicsemi.android.common.analytics.view

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import no.nordicsemi.android.common.analytics.R

@Composable
fun AnalyticsPermissionButton() {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    IconButton(onClick = { showDialog = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_firebase),
            contentDescription = stringResource(id = R.string.analytics_permission_button),
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
    if (showDialog) {
        AnalyticsPermissionSwitchDialog(onDismiss = { showDialog = false })
    }
}