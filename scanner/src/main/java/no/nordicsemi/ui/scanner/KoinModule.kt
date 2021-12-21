package no.nordicsemi.ui.scanner

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.ParcelUuid
import no.nordicsemi.ui.scanner.navigation.viewmodel.ScannerNavigationViewModel
import no.nordicsemi.ui.scanner.scanner.repository.DevicesDataStore
import no.nordicsemi.ui.scanner.scanner.repository.DevicesRepository
import no.nordicsemi.ui.scanner.scanner.viewmodel.ScannerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val scannerModule = module {
    viewModel { ScannerViewModel(get(), get()) }
    viewModel { ScannerNavigationViewModel(get(), get()) }

    single { Utils(get(), get(), getBleAdapter(get())) }
    single { LocalDataProvider(get()) }
    single { DevicesDataStore() }
    single { DevicesRepository(get(), get(), get()) }
}

private fun getBleAdapter(context: Context): BluetoothAdapter {
    val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    return manager.adapter
}
