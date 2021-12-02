package no.nordicsemi.ui.scanner.scanner.repository

sealed class DeviceResource<T> {

    companion object {
        fun <T> createLoading() = LoadingResult<T>()
        fun <T> createSuccess(value: T) = SuccessResult(value)
        fun <T> createError(errorCode: Int) = ErrorResult<T>(errorCode)
    }
}

class LoadingResult <T> : DeviceResource<T>()

data class SuccessResult<T>(val value: T) : DeviceResource<T>()

data class ErrorResult<T>(val errorCode: Int) : DeviceResource<T>()
