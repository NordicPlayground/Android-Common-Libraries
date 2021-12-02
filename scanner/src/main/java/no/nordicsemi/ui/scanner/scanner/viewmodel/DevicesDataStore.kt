package no.nordicsemi.ui.scanner.scanner.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.toDiscoveredBluetoothDevice

class DevicesDataStore {

    val devices = mutableListOf<DiscoveredBluetoothDevice>()
    val data = MutableStateFlow(devices.toList())

    fun addNewDevice(scanResult: ScanResult) {
        data.value.find { it.device == scanResult.device }
            ?.apply { update(scanResult) }
            ?: scanResult.toDiscoveredBluetoothDevice().also { devices.add(it) }

        data.value = devices.toList()
    }

    fun clear() {
        devices.clear()
        data.value = devices
    }
}

data class DevicesScanFilter(
    val filterUuidRequired: Boolean,
    val filterNearbyOnly: Boolean
)
