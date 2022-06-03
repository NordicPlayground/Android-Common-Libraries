package no.nordicsemi.analytics.view

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.analytics.R
import no.nordicsemi.analytics.repository.AnalyticsPermissionData
import no.nordicsemi.analytics.viewmodel.AnalyticsPermissionViewModel
import no.nordicsemi.android.material.you.parseBold

@Composable
fun AnalyticsPermissionRequestDialog() {

    val viewModel: AnalyticsPermissionViewModel = hiltViewModel()
    val data = viewModel.permissionData.collectAsState(AnalyticsPermissionData(false, false)).value

    if (!data.wasInfoDialogShown) {
        AlertDialog(
            onDismissRequest = { viewModel.onDeclineButtonClick() },
            title = { Text(stringResource(id = R.string.analytics_dialog_title)) },
            text = {
                Text(
                    text = stringResource(id = R.string.analytics_dialog_info).parseBold(),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.onConfirmButtonClick() }) {
                    Text(stringResource(id = R.string.analytics_dialog_accept))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDeclineButtonClick() }) {
                    Text(stringResource(id = R.string.analytics_dialog_decline))
                }
            }
        )
    }
}
