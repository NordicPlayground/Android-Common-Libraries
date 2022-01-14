package no.nordicsemi.ui.scanner.permissions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.ui.scanner.R
import no.nordicsemi.ui.scanner.navigation.view.FindDeviceCloseResult
import no.nordicsemi.ui.scanner.navigation.view.FindDeviceFlowStatus
import no.nordicsemi.ui.scanner.ui.AppBar

@Composable
internal fun BluetoothNotAvailableView(onResult: (FindDeviceFlowStatus) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        AppBar(stringResource(id = R.string.bluetooth)) {
            onResult(FindDeviceCloseResult)
        }

        Image(
            painter = painterResource(id = R.drawable.ic_bluetooth_disabled),
            contentDescription = "",
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = stringResource(id = R.string.bluetooth_not_available_title),
            color = MaterialTheme.colorScheme.secondary,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.bluetooth_not_available_info),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun BluetoothNotAvailableView_Preview() {
    BluetoothNotAvailableView { }
}
