package no.nordicsemi.ui.scanner

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.ui.scanner.bonding.repository.BondingState

fun ScanResult.toDiscoveredBluetoothDevice(): DiscoveredBluetoothDevice {
    return DiscoveredBluetoothDevice(
        device = device,
        scanResult = this,
        name = if (scanRecord != null) scanRecord!!.deviceName else null,
        previousRssi = rssi,
        rssi = rssi,
        highestRssi = rssi
    )
}

@SuppressLint("MissingPermission")
@Parcelize
data class DiscoveredBluetoothDevice(
    val device: BluetoothDevice,
    val scanResult: ScanResult? = null,
    val name: String? = null,
    val lastScanResult: ScanResult? = null,
    val rssi: Int = 0,
    val previousRssi: Int = 0,
    val highestRssi: Int = 0,
) : Parcelable {

    fun hasRssiLevelChanged(): Boolean {
        val newLevel =
            if (rssi <= 10) 0 else if (rssi <= 28) 1 else if (rssi <= 45) 2 else if (rssi <= 65) 3 else 4
        val oldLevel =
            if (previousRssi <= 10) 0 else if (previousRssi <= 28) 1 else if (previousRssi <= 45) 2 else if (previousRssi <= 65) 3 else 4
        return newLevel != oldLevel
    }

    fun update(scanResult: ScanResult): DiscoveredBluetoothDevice {
        return copy(
            device = scanResult.device,
            lastScanResult = scanResult,
            name = scanResult.scanRecord?.deviceName,
            previousRssi = rssi,
            rssi = scanResult.rssi,
            highestRssi = if (highestRssi > rssi) highestRssi else rssi
        )
    }

    fun matches(scanResult: ScanResult): Boolean {
        return device.address == scanResult.device.address
    }

    fun getBondingState(): BondingState {
        return when (device.bondState) {
            BluetoothDevice.BOND_BONDED -> BondingState.BONDED
            BluetoothDevice.BOND_BONDING -> BondingState.BONDING
            else -> BondingState.NONE
        }
    }

    fun createBond() {
        device.createBond()
    }

    fun displayName(): String? {
        return when {
            name?.isNotEmpty() == true -> name
            device.name?.isNotEmpty() == true -> device.name
            else -> null
        }
    }

    fun address(): String {
        return device.address
    }

    fun displayAddress(): String {
        return when {
            else -> device.address
        }
    }

    override fun hashCode(): Int {
        return device.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is DiscoveredBluetoothDevice) {
            return device.address == other.device.address
        }
        return super.equals(other)
    }
}
