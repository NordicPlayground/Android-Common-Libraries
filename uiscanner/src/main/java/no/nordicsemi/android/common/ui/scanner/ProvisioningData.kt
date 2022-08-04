package no.nordicsemi.android.common.ui.scanner

import android.os.ParcelUuid
import no.nordicsemi.android.support.v18.scanner.ScanResult
import java.util.*

data class ProvisioningData(
    val version: Int,
    val isProvisioned: Boolean,
    val isConnected: Boolean,
    val rssi: Int
) {
    constructor(data: ByteArray) : this(
        version = data[0].toInt(),
        // 2 bytes reserved for flags
        isProvisioned = data[1].toInt() and 0x01 != 0,
        isConnected = data[1].toInt() and 0x02 != 0,
        rssi = data[3].toInt()
    )

    companion object {
        private val parcelUuid = ParcelUuid(UUID.fromString("14387800-130c-49e7-b877-2881c89cb258"))

        fun create(scanResult: ScanResult) = scanResult.scanRecord?.serviceData
            ?.get(parcelUuid)
            ?.let { ProvisioningData(it) }
    }
}
