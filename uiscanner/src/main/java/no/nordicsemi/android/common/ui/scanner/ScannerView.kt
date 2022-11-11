package no.nordicsemi.android.common.ui.scanner

import android.os.ParcelUuid
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import no.nordicsemi.android.common.permission.RequireBluetooth
import no.nordicsemi.android.common.permission.RequireLocation
import no.nordicsemi.android.common.ui.scanner.main.DeviceListItem
import no.nordicsemi.android.common.ui.scanner.main.DevicesListView
import no.nordicsemi.android.common.ui.scanner.main.viewmodel.ScannerViewModel
import no.nordicsemi.android.common.ui.scanner.model.DiscoveredBluetoothDevice
import no.nordicsemi.android.common.ui.scanner.view.internal.FilterView

@Composable
fun ScannerView(
    uuid: ParcelUuid?,
    onScanningStateChanged: (Boolean) -> Unit = {},
    onResult: (DiscoveredBluetoothDevice) -> Unit,
    deviceItem: @Composable (DiscoveredBluetoothDevice) -> Unit = {
        DeviceListItem(it.displayName, it.address)
    }
) {
    RequireBluetooth(
        onChanged = { onScanningStateChanged(it) }
    ) {
        RequireLocation(
            onChanged = { onScanningStateChanged(it) }
        ) { isLocationRequiredAndDisabled ->
            val viewModel = hiltViewModel<ScannerViewModel>()
                .apply { setFilterUuid(uuid) }

            val state by viewModel.state.collectAsState()
            val config by viewModel.filterConfig.collectAsState()

            Column(modifier = Modifier.fillMaxSize()) {
                FilterView(
                    config = config,
                    onChanged = { viewModel.setFilter(it) }
                )

                val swipeRefreshState = rememberSwipeRefreshState(false)

                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { viewModel.refresh() },
                ) {
                    DevicesListView(
                        isLocationRequiredAndDisabled = isLocationRequiredAndDisabled,
                        state = state,
                        modifier = Modifier.fillMaxSize(),
                        onClick = { onResult(it) },
                        deviceItem = deviceItem,
                    )
                }
            }
        }
    }
}