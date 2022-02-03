package no.nordicsemi.ui.scanner.scanner.repository

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.LocalDataProvider
import no.nordicsemi.ui.scanner.Utils
import javax.inject.Inject

@ViewModelScoped
internal class DevicesRepository @Inject constructor(
    private val utils: Utils,
    private val dataProvider: LocalDataProvider,
    private val devicesDataStore: DevicesDataStore
) {

    fun getDevices(): Flow<AllDevices> = callbackFlow<DeviceResource<List<DiscoveredBluetoothDevice>>> {
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
            .setLegacy(false)
            .setReportDelay(500)
            .setUseHardwareBatchingIfSupported(false)
            .build()
        val scanner = BluetoothLeScannerCompat.getScanner()
        scanner.startScan(null, settings, scanCallback)

        awaitClose {
            scanner.stopScan(scanCallback)
        }
    }.map { AllDevices(devicesDataStore.bondedDevices, it) }

    fun clear() {
        devicesDataStore.clear()
    }
}
