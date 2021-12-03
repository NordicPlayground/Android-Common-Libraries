package no.nordicsemi.ui.scanner.scanner.repository

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
