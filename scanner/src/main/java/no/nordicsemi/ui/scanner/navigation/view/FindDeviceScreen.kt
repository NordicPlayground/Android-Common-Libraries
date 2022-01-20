package no.nordicsemi.ui.scanner.navigation.view

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager.MODE_CHANGED_ACTION
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.ui.scanner.navigation.viewmodel.*
import no.nordicsemi.ui.scanner.permissions.*
import no.nordicsemi.ui.scanner.scanner.view.ScannerScreen

@Composable
fun FindDeviceScreen() {
    val viewModel = hiltViewModel<ScannerNavigationViewModel>()

    val destination = viewModel.destination.collectAsState().value

    val context = LocalContext.current
    val activity = context as Activity
    BackHandler { viewModel.onEvent(NavigateUp) }

    val onEvent = { event: PermissionsViewEvent ->
        viewModel.onEvent(event)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (destination) {
            BluetoothDisabledDestination -> BluetoothDisabledView(onEvent)
            BluetoothNotAvailableDestination -> BluetoothNotAvailableView(onEvent)
            BluetoothPermissionRequiredDestination -> BluetoothPermissionRequiredView(
                viewModel.utils.isBluetoothScanPermissionDeniedForever(
                    activity
                ),
                onEvent
            )
            LocationPermissionRequiredDestination -> LocationPermissionRequiredView(
                viewModel.utils.isLocationPermissionDeniedForever(
                    activity
                ),
                onEvent
            )
            PeripheralDeviceRequiredDestination -> ScannerScreen(viewModel.filterId, onEvent)
        }
    }

    registerReceiver(IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    registerReceiver(IntentFilter(MODE_CHANGED_ACTION))
}

@SuppressLint("ComposableNaming")
@Composable
private fun registerReceiver(intentFilter: IntentFilter) {
    val viewModel = hiltViewModel<ScannerNavigationViewModel>()
    val context = LocalContext.current

    DisposableEffect(context) {
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                viewModel.onEvent(RefreshNavigation)
            }
        }

        context.registerReceiver(broadcast, intentFilter)

        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}
