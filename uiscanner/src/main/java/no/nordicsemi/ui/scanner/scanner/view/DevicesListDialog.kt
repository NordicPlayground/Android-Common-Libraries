package no.nordicsemi.ui.scanner.scanner.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.flowlayout.FlowRow
import no.nordicsemi.android.material.you.CheckboxFallback
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.R
import no.nordicsemi.ui.scanner.scanner.repository.AllDevices
import no.nordicsemi.ui.scanner.scanner.repository.ErrorResult
import no.nordicsemi.ui.scanner.scanner.repository.LoadingResult
import no.nordicsemi.ui.scanner.scanner.repository.SuccessResult
import no.nordicsemi.ui.scanner.ui.FlowCanceled
import no.nordicsemi.ui.scanner.ui.ItemSelectedResult
import no.nordicsemi.ui.scanner.ui.StringListDialogConfig
import no.nordicsemi.ui.scanner.ui.exhaustive

@Composable
internal fun DevicesListDialog(requireLocation: Boolean, config: StringListDialogConfig) {
    Dialog(onDismissRequest = { config.onResult(FlowCanceled) }) {
        DevicesListView(requireLocation, config)
    }
}

@Composable
internal fun DevicesListView(requireLocation: Boolean, config: StringListDialogConfig) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.filters),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            FlowRow {
                config.filterItems.forEachIndexed { i, item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CheckboxFallback(checked = item.isChecked, onCheckedChange = {
                            config.onFilterItemCheckChanged(i)
                        })
                        Spacer(modifier = Modifier.height(4.dp))
                        item.icon?.let {
                            Image(
                                painter = painterResource(id = it),
                                contentDescription = ""
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.text)
                    }
                }
            }
        }

        if (config.result.size() == 0) {
            item { ScanEmptyView(requireLocation) }
        } else {
            when (config.result.discoveredDevices) {
                is LoadingResult -> item { ScanEmptyView(requireLocation) }
                is SuccessResult -> DevicesSection(config.result, config)
                is ErrorResult -> item { ErrorSection() }
            }.exhaustive
        }

        item { Spacer(modifier = Modifier.size(16.dp)) }
    }
}

private fun LazyListScope.DevicesSection(
    items: AllDevices,
    config: StringListDialogConfig
) {
    if (items.bondedDevices.isNotEmpty()) {
        item {
            Text(
                text = stringResource(id = R.string.bonded_devices),
                style = MaterialTheme.typography.titleLarge
            )
        }
        items(items.bondedDevices.size) { i ->
            DeviceItem(device = items.bondedDevices[i], config = config)
        }
    }

    val discoveredDevices = items.discoveredDevices as? SuccessResult

    discoveredDevices?.let {
        val devices = it.value
        if (devices.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.discovered_devices),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            items(devices.size) { i ->
                DeviceItem(device = devices[i], config = config)
            }
        }
    }
}

@Composable
private fun DeviceItem(
    device: DiscoveredBluetoothDevice,
    config: StringListDialogConfig
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { config.onResult(ItemSelectedResult(device)) }
            .padding(8.dp),
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            config.leftIcon?.let {
                Image(
                    painter = painterResource(it),
                    contentDescription = "Content image",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary),
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CircleShape
                        )
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.size(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                val deviceName = device.displayName()
                if (deviceName != null) {
                    Text(
                        text = deviceName,
                        style = MaterialTheme.typography.titleMedium
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.device_no_name),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Text(text = device.displayAddress(), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun ErrorSection() {
    Text(
        text = stringResource(id = R.string.scan_failed),
        color = MaterialTheme.colorScheme.error
    )
}