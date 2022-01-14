package no.nordicsemi.ui.scanner.permissions

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
internal fun BluetoothDisabledView(onResult: (FindDeviceFlowStatus) -> Unit) {
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
            text = stringResource(id = R.string.bluetooth_disabled_title),
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.bluetooth_disabled_info),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current
        Button(onClick = { enableBluetooth(context) }) {
            Text(text = stringResource(id = R.string.action_enable))
        }
    }
}

private fun enableBluetooth(context: Context) {
    context.startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
}

@Preview
@Composable
private fun BluetoothDisabledView_Preview() {
    BluetoothDisabledView { }
}
