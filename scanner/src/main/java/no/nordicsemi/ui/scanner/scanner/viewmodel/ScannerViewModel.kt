package no.nordicsemi.ui.scanner.scanner.viewmodel

import android.bluetooth.BluetoothDevice
import android.os.ParcelUuid
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.LocalDataProvider
import no.nordicsemi.ui.scanner.scanner.repository.DevicesRepository
import no.nordicsemi.ui.scanner.scanner.repository.SuccessResult
import java.util.*

private val LBS_UUID_SERVICE = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")
private val FILTER_UUID = ParcelUuid(LBS_UUID_SERVICE)
private const val FILTER_RSSI = -50 // [dBm]

class ScannerViewModel(
    private val dataProvider: LocalDataProvider,
    devicesRepository: DevicesRepository
) : ViewModel() {

    private val config = MutableStateFlow(
        DevicesScanFilter(
            filterUuidRequired = false,
            filterNearbyOnly = false
        )
    )

    val devices = config.combine(devicesRepository.getDevices()) { config, result ->
        (result as? SuccessResult<List<DiscoveredBluetoothDevice>>)?.let {
            val newItems = it.value.filter {
                !config.filterUuidRequired || it.scanResult.scanRecord?.serviceUuids?.contains(
                    FILTER_UUID
                ) ?: false
            }.filter {
                !config.filterNearbyOnly || it.rssi >= FILTER_RSSI
            }
            SuccessResult(newItems)
        } ?: result
    }

    fun onDeviceSelected(device: BluetoothDevice) {
        dataProvider.device = device
    }

    fun filterByUuid(uuidRequired: Boolean) {
        config.value = config.value.copy(filterUuidRequired = uuidRequired)
    }

    fun filterByDistance(nearbyOnly: Boolean) {
        config.value = config.value.copy(filterNearbyOnly = nearbyOnly)
    }
}
