package no.nordicsemi.ui.scanner.navigation.view

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager.MODE_CHANGED_ACTION
import android.os.ParcelUuid
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.Utils
import no.nordicsemi.ui.scanner.navigation.viewmodel.BluetoothDisabledDestination
import no.nordicsemi.ui.scanner.navigation.viewmodel.BluetoothNotAvailableDestination
import no.nordicsemi.ui.scanner.navigation.viewmodel.BluetoothPermissionRequiredDestination
import no.nordicsemi.ui.scanner.navigation.viewmodel.FinishDestination
import no.nordicsemi.ui.scanner.navigation.viewmodel.LocationPermissionRequiredDestination
import no.nordicsemi.ui.scanner.navigation.viewmodel.PeripheralDeviceRequiredDestination
import no.nordicsemi.ui.scanner.navigation.viewmodel.ScannerNavigationViewModel
import no.nordicsemi.ui.scanner.permissions.BluetoothDisabledView
import no.nordicsemi.ui.scanner.permissions.BluetoothNotAvailableView
import no.nordicsemi.ui.scanner.permissions.BluetoothPermissionRequiredView
import no.nordicsemi.ui.scanner.permissions.LocationPermissionRequiredView
import no.nordicsemi.ui.scanner.scanner.view.ScannerScreen
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@Composable
fun FindDeviceScreen(uuid: ParcelUuid, onDeviceFound: @Composable (DiscoveredBluetoothDevice) -> Unit) {
    val viewModel = getViewModel<ScannerNavigationViewModel>()
    val utils = get<Utils>()

    val destination = viewModel.destination.collectAsState().value
    val refreshNavigation = { viewModel.refreshNavigation() }

    val context = LocalContext.current
    val activity = context as Activity
    BackHandler { activity.finish() }

    (destination as? FinishDestination)?.let {
        onDeviceFound(destination.device)
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        when (destination) {
            BluetoothDisabledDestination -> BluetoothDisabledView()
            BluetoothNotAvailableDestination -> BluetoothNotAvailableView()
            BluetoothPermissionRequiredDestination -> BluetoothPermissionRequiredView(isDeniedForever = utils.isBluetoothScanPermissionDeniedForever(activity), refreshNavigation)
            LocationPermissionRequiredDestination -> LocationPermissionRequiredView(isDeniedForever = utils.isLocationPermissionDeniedForever(activity), refreshNavigation)
            PeripheralDeviceRequiredDestination -> ScannerScreen(uuid, refreshNavigation)
        }
    }

    registerReceiver(IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    registerReceiver(IntentFilter(MODE_CHANGED_ACTION))
}

@Composable
private fun registerReceiver(intentFilter: IntentFilter) {
    val viewModel = getViewModel<ScannerNavigationViewModel>()
    val context = LocalContext.current

    DisposableEffect(context) {
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                viewModel.refreshNavigation()
            }
        }

        context.registerReceiver(broadcast, intentFilter)

        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}

@Composable
private fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    val currentOnBack = rememberUpdatedState(onBack)
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack.value()
            }
        }
    }
    SideEffect {
        backCallback.isEnabled = enabled
    }
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}
