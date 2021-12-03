package no.nordicsemi.ui.scanner.scanner.repository

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.LocalDataProvider
import no.nordicsemi.ui.scanner.Utils

internal class DevicesRepository(
    private val utils: Utils,
    private val dataProvider: LocalDataProvider,
    private val devicesDataStore: DevicesDataStore
) {

    fun getDevices() = callbackFlow<DeviceResource<List<DiscoveredBluetoothDevice>>> {
        val scanCallback: ScanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                // This callback will be called only if the scan report delay is not set or is set to 0.

                // If the packet has been obtained while Location was disabled, mark Location as not required
                if (utils.isLocationPermissionRequired && !utils.isLocationEnabled()) {
                    dataProvider.isLocationPermissionRequired = false
                }
                devicesDataStore.addNewDevice(result)

                trySend(DeviceResource.createSuccess(devicesDataStore.devices))
            }

            override fun onBatchScanResults(results: List<ScanResult>) {
                // This callback will be called only if the report delay set above is greater then 0.

                // If the packet has been obtained while Location was disabled, mark Location as not required
                if (utils.isLocationPermissionRequired && !utils.isLocationEnabled()) {
                    dataProvider.isLocationPermissionRequired = false
                }
                results.forEach {
                    devicesDataStore.addNewDevice(it)

                }
                trySend(DeviceResource.createSuccess(devicesDataStore.devices))
            }

            override fun onScanFailed(errorCode: Int) {
                trySend(DeviceResource.createError(errorCode))
            }
        }

        trySend(DeviceResource.createLoading())

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(500)
            .setUseHardwareBatchingIfSupported(false)
            .build()
        val scanner = BluetoothLeScannerCompat.getScanner()
        scanner.startScan(null, settings, scanCallback)

        awaitClose {
            scanner.stopScan(scanCallback)
        }
    }
}
