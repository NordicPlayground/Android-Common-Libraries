package no.nordicsemi.android.common.scanner.data

import no.nordicsemi.kotlin.ble.client.android.Peripheral
import no.nordicsemi.kotlin.ble.client.android.ScanResult
import kotlin.math.max

/**
 * A record containing information about a scanned peripheral.
 *
 * @property peripheral The peripheral instance.
 * @property latestScanResult The latest scan result received from the peripheral.
 * @property highestRssi The highest RSSI value recorded for this peripheral during the scanning session.
 */
data class ScannedPeripheral(
    val peripheral: Peripheral,
    var latestScanResult: ScanResult,
    var highestRssi: Int,
) {
    constructor(scanResult: ScanResult, previousHighestRssi: Int = -128) : this(
        peripheral = scanResult.peripheral,
        latestScanResult = scanResult,
        highestRssi = max(previousHighestRssi, scanResult.rssi)
    )

    override fun equals(other: Any?): Boolean {
        return other is ScannedPeripheral &&
               other.peripheral == peripheral &&
               other.latestScanResult.rssi == latestScanResult.rssi
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}