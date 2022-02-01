package no.nordicsemi.ui.scanner.scanner.repository

import android.bluetooth.BluetoothAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.toDiscoveredBluetoothDevice
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DevicesDataStore @Inject constructor(
    private val bleAdapter: BluetoothAdapter
) {

    val bondedDevices
        get() = bleAdapter.bondedDevices.map { DiscoveredBluetoothDevice(it) }
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

internal data class DevicesScanFilter(
    val filterUuidRequired: Boolean,
    val filterNearbyOnly: Boolean
)
