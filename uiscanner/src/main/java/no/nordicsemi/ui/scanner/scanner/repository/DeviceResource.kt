package no.nordicsemi.ui.scanner.scanner.repository

import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice

internal data class AllDevices(
    val bondedDevices: List<DiscoveredBluetoothDevice> = emptyList(),
    val discoveredDevices: DeviceResource<List<DiscoveredBluetoothDevice>> = LoadingResult()
) {
    fun size(): Int {
        return bondedDevices.size + when (discoveredDevices) {
            is ErrorResult -> 0
            is LoadingResult -> 0
            is SuccessResult -> discoveredDevices.value.size
        }
    }
}

internal sealed class DeviceResource<T> {

    companion object {
        fun <T> createLoading() = LoadingResult<T>()
        fun <T> createSuccess(value: T) = SuccessResult(value)
        fun <T> createError(errorCode: Int) = ErrorResult<T>(errorCode)
    }
}

internal class LoadingResult <T> : DeviceResource<T>()

internal data class SuccessResult<T>(val value: T) : DeviceResource<T>()

internal data class ErrorResult<T>(val errorCode: Int) : DeviceResource<T>()
