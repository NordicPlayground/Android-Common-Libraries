package no.nordicsemi.android.common.test.main.page

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.nordicsemi.android.common.navigation.Navigator
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.PagerViewItem
import no.nordicsemi.android.common.theme.view.RssiIcon
import no.nordicsemi.android.common.ui.scanner.main.DeviceListItem
import no.nordicsemi.android.common.ui.scanner.model.DiscoveredBluetoothDevice
import javax.inject.Inject

val BasicsPage = PagerViewItem("Basics") {
    val vm = hiltViewModel<BasicPageViewModel>()
    val device by vm.device
    BasicViewsScreen(
        device = device?.let { DeviceInfo(it) },
        onOpenScanner = { vm.openScanner() }
    )
}

@HiltViewModel
class BasicPageViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel() {
    var device = mutableStateOf(null as DiscoveredBluetoothDevice?)

    fun openScanner() {
        // As the "navigateForResult" coroutine uses navigation,
        // it must be launched on the main dispatcher
        viewModelScope.launch(Dispatchers.IO) {
            // Navigate and wait for the result.
            navigator.navigateForResult<DiscoveredBluetoothDevice>()
                // If the result was returned, update the device.
                // If user cancelled, the result will be null and we ignore it.
                ?.let { device.value = it }
        }
    }
}

@SuppressLint("MissingPermission")
data class DeviceInfo(
    private val device: DiscoveredBluetoothDevice?
) {
    val name = device?.name ?: "Unknown"
    val address = device?.address ?: "Unknown"
    val rssi = device?.rssi ?: 0
}

@Composable
private fun BasicViewsScreen(
    device: DeviceInfo?,
    onOpenScanner: () -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onOpenScanner
        ) {
            Text(text = stringResource(id = R.string.action_scanner))
        }

        device?.let {
            Device(
                deviceName = it.name,
                deviceAddress = it.address,
                rssi = it.rssi,
            )
        }

        BasicViewsScreen()

        Cards()
    }
}

@Composable
private fun Device(
    deviceName: String,
    deviceAddress: String,
    rssi: Int,
) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(10.dp))
        .clickable { }
        .padding(8.dp)
    ) {
        DeviceListItem(
            name = deviceName,
            address = deviceAddress,
        ) {
            RssiIcon(rssi = rssi)
        }
    }
}

@Composable
private fun Buttons() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(onClick = { }) {
            Text(text = stringResource(id = R.string.action_no_op))
        }

        Button(
            onClick = { },
            enabled = false,
        ) {
            Text(text = stringResource(id = R.string.action_disabled))
        }

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text(text = stringResource(id = R.string.action_destroy))
        }
    }
}

@Composable
private fun OtherWidgets() {
    Column {
        var checkedCheckbox by rememberSaveable { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { checkedCheckbox = !checkedCheckbox },
        ) {

            Text(
                text = stringResource(id = R.string.option),
                modifier = Modifier.weight(1.0f),
            )

            Checkbox(checked = checkedCheckbox, onCheckedChange = { checkedCheckbox = it })
        }

        var checkedSwitch by rememberSaveable { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { checkedSwitch = !checkedSwitch },
        ) {

            Text(
                text = stringResource(id = R.string.option),
                modifier = Modifier.weight(1.0f),
            )

            Switch(checked = checkedSwitch, onCheckedChange = { checkedSwitch = it })
        }
    }
}

@Composable
private fun BasicViewsScreen() {
    Column {
        Buttons()

        Spacer(modifier = Modifier.height(16.dp))

        OtherWidgets()
    }
}

@Composable
private fun Cards() {
    Column {
        OutlinedCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(id = R.string.text_card_outlined))

                Spacer(modifier = Modifier.height(16.dp))

                Buttons()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(id = R.string.text_card))

                Spacer(modifier = Modifier.height(16.dp))

                Buttons()
            }
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    NordicTheme {
        BasicViewsScreen(
            device = null,
            onOpenScanner = {}
        )
    }
}