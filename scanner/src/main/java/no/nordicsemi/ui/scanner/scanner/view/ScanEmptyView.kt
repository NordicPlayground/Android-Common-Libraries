package no.nordicsemi.ui.scanner.scanner.view

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.ui.scanner.LocalDataProvider
import no.nordicsemi.ui.scanner.R
import org.koin.androidx.compose.get

@Composable
fun ScanEmptyView(showDialog: () -> Unit) {
    val dataProvider = get<LocalDataProvider>()
    val requireLocation = dataProvider.locationState.collectAsState().value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_bluetooth_disabled),
            contentDescription = "",
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = stringResource(id = R.string.no_device_guide_title),
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(id = R.string.no_device_guide_info))

        if (requireLocation) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = stringResource(id = R.string.no_device_guide_location_info))

            Spacer(modifier = Modifier.height(16.dp))

            val context = LocalContext.current
            Button(onClick = { openLocationSettings(context) }) {
                Text(text = stringResource(id = R.string.action_location_settings))
            }
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(onClick = { showDialog() }) {
            Text(text = stringResource(id = R.string.action_try_again))
        }
    }
}

private fun openLocationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

@Preview
@Composable
fun ScanEmptyView_Preview() {
    ScanEmptyView { }
}
