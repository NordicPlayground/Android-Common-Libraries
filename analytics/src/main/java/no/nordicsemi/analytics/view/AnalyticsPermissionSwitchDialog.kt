package no.nordicsemi.analytics.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.analytics.R
import no.nordicsemi.analytics.repository.AnalyticsPermissionData
import no.nordicsemi.analytics.viewmodel.AnalyticsPermissionViewModel
import no.nordicsemi.android.material.you.CheckboxFallback
import no.nordicsemi.android.material.you.parseBold

@Composable
fun AnalyticsPermissionButton() {

    val showDialog = rememberSaveable { mutableStateOf(false) }

    IconButton(onClick = { showDialog.value = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_firebase),
            contentDescription = stringResource(id = R.string.analytics_permission_button),
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
    AnalyticsPermissionSwitchDialog(showDialog)
}

@Composable
fun AnalyticsPermissionSwitchDialog(showDialog: MutableState<Boolean>) {

    val viewModel: AnalyticsPermissionViewModel = hiltViewModel()
    val data = viewModel.permissionData.collectAsState(AnalyticsPermissionData()).value

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { viewModel.onDeclineButtonClick() },
            title = { Text(stringResource(id = R.string.analytics_switch_dialog_title)) },
            text = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            text = stringResource(id = R.string.analytics_switch_dialog_permission),
                            style = MaterialTheme.typography.titleMedium
                        )
                        CheckboxFallback(
                            checked = data.isPermissionGranted,
                            onCheckedChange = { viewModel.onPermissionChanged() }
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.analytics_dialog_info).parseBold(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(stringResource(id = R.string.analytics_switch_dialog_button))
                }
            },
            modifier = Modifier.fillMaxHeight(0.9f).fillMaxWidth(0.95f)
        )
    }
}
