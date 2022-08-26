package no.nordicsemi.android.common.navigation

import kotlinx.coroutines.flow.MutableStateFlow

class DestinationMapStateFlow<T>(
    private val state: MutableStateFlow<Map<DestinationId, T>>
) : MutableStateFlow<Map<DestinationId, T>> by state {

    constructor(map: Map<DestinationId, T>) : this(MutableStateFlow(map))

    override suspend fun emit(newValue: Map<DestinationId, T>) {
        state.emit(newValue.toMap())
    }

    override fun tryEmit(value: Map<DestinationId, T>): Boolean {
        return state.tryEmit(value.toMap())
    }
}
