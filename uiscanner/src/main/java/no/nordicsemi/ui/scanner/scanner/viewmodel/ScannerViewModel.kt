package no.nordicsemi.ui.scanner.scanner.viewmodel

import android.os.ParcelUuid
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.LocalDataProvider
import no.nordicsemi.ui.scanner.scanner.repository.AllDevices
import no.nordicsemi.ui.scanner.scanner.repository.DevicesRepository
import no.nordicsemi.ui.scanner.scanner.repository.DevicesScanFilter
import no.nordicsemi.ui.scanner.scanner.repository.SuccessResult
import javax.inject.Inject

private const val FILTER_RSSI = -50 // [dBm]

@HiltViewModel
internal class ScannerViewModel @Inject constructor(
    val dataProvider: LocalDataProvider,
    private val devicesRepository: DevicesRepository,
) : ViewModel() {

    private lateinit var uuid: ParcelUuid

    val config = MutableStateFlow(
        DevicesScanFilter(
            filterUuidRequired = true,
            filterNearbyOnly = false
        )
    )

    val devices = config.combine(devicesRepository.getDevices()) { config, result ->
        val devices = (result.discoveredDevices as? SuccessResult<List<DiscoveredBluetoothDevice>>)?.let {
            val newItems = it.value.filter {
                !config.filterUuidRequired || it.scanResult?.scanRecord?.serviceUuids?.contains(uuid) ?: false
            }.filter {
                !config.filterNearbyOnly || it.rssi >= FILTER_RSSI
            }
            SuccessResult(newItems.filter { !result.bondedDevices.contains(it) })
        } ?: result.discoveredDevices
        result.copy(discoveredDevices = devices)
    }.stateIn(viewModelScope, SharingStarted.Lazily, AllDevices())

    fun setFilterUuid(uuid: ParcelUuid) {
        this.uuid = uuid
    }

    fun filterByUuid(uuidRequired: Boolean) {
        config.value = config.value.copy(filterUuidRequired = uuidRequired)
    }

    fun filterByDistance(nearbyOnly: Boolean) {
        config.value = config.value.copy(filterNearbyOnly = nearbyOnly)
    }

    override fun onCleared() {
        super.onCleared()
        devicesRepository.clear()
    }
}
