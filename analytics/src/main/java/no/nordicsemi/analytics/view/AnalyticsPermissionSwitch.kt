package no.nordicsemi.analytics.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.analytics.R
import no.nordicsemi.analytics.repository.AnalyticsPermissionData
import no.nordicsemi.analytics.viewmodel.AnalyticsPermissionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsPermissionSwitch() {
    val viewModel: AnalyticsPermissionViewModel = hiltViewModel()
    val state = viewModel.permissionData.collectAsState(initial = AnalyticsPermissionData()).value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.onPermissionChanged() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.analytics_switch_title),
            style = MaterialTheme.typography.titleLarge,
        )

        Checkbox(checked = state.isPermissionGranted, onCheckedChange = {
            viewModel.onPermissionChanged()
        })
    }
}

@Preview
@Composable
fun AnalyticsPermissionSwitch_Preview() {
    AnalyticsPermissionSwitch()
}
