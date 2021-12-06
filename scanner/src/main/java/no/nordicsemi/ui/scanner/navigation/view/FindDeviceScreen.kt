package no.nordicsemi.ui.scanner.navigation.view

import android.app.Activity
import android.os.ParcelUuid
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.LocalDataProvider
import no.nordicsemi.ui.scanner.Utils
import no.nordicsemi.ui.scanner.navigation.viewmodel.*
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
    val localDataProvider = get<LocalDataProvider>()

    val destination = viewModel.destination.collectAsState().value
    val refreshNavigation = { viewModel.refreshNavigation() }

    val context = LocalContext.current
    val activity = context as Activity
    BackHandler { activity.finish() }

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = destination.id) {
        composable(LocationPermissionRequiredDestination.id) {
            LocationPermissionRequiredView(isDeniedForever = utils.isLocationPermissionDeniedForever(activity), refreshNavigation)
        }
        composable(BluetoothPermissionRequiredDestination.id) {
            BluetoothPermissionRequiredView(isDeniedForever = utils.isBluetoothScanPermissionDeniedForever(activity), refreshNavigation)
        }
        composable(BluetoothNotAvailableDestination.id) {
            BluetoothNotAvailableView()
        }
        composable(BluetoothDisabledDestination.id) {
            BluetoothDisabledView()
        }
        composable(PeripheralDeviceRequiredDestination.id) {
            ScannerScreen(uuid, refreshNavigation)
        }
        /**
         * fixme for some reason this block cannot be placed in below launchedeffect because it want to invoke below code before
         *  navigate is actually called
         */
        composable("finish") {
            (destination as? FinishDestination)?.let {
                onDeviceFound(it.device)
            }
        }
    }

    LaunchedEffect(destination) {
        navController.navigate(destination.id)
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
