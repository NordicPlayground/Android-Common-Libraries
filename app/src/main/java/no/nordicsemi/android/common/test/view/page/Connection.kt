package no.nordicsemi.android.common.test.view.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.theme.view.PagerViewItem
import no.nordicsemi.android.common.ui.scanner.view.DeviceConnectingView
import no.nordicsemi.android.common.ui.scanner.view.DeviceDisconnectedView
import no.nordicsemi.android.common.ui.scanner.view.NoDeviceView
import no.nordicsemi.android.common.ui.scanner.view.Reason

val Connection = PagerViewItem("Connection") {
    Connection()
}

@Composable
private fun Connection() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        NoDeviceView()

        DeviceConnectingView { padding ->
            Button(
                onClick = { },
                modifier = Modifier.padding(padding),
            ) {
                Text(text = stringResource(id = R.string.action_no_op))
            }
        }

        DeviceDisconnectedView(reason = Reason.LINK_LOSS) { padding ->
            Button(
                onClick = { },
                modifier = Modifier.padding(padding),
            ) {
                Text(text = stringResource(id = R.string.action_no_op))
            }
        }

        DeviceDisconnectedView(reason = Reason.MISSING_SERVICE) { padding ->
            Button(
                onClick = { },
                modifier = Modifier.padding(padding),
            ) {
                Text(text = stringResource(id = R.string.action_no_op))
            }
        }

        DeviceDisconnectedView(reason = Reason.USER) { padding ->
            Button(
                onClick = { },
                modifier = Modifier.padding(padding),
            ) {
                Text(text = stringResource(id = R.string.action_no_op))
            }
        }
    }
}

@Preview
@Composable
private fun ConnectionPreview() {
    Connection()
}