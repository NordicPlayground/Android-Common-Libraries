package no.nordicsemi.android.common.navigation

import kotlinx.coroutines.flow.MutableStateFlow

class DestinationMapStateFlow<T>(
    private val state: MutableStateFlow<Map<DestinationId, T>>
) : MutableStateFlow<Map<DestinationId, T>> by state {

    constructor(map: Map<DestinationId, T>) : this(MutableStateFlow(map))

    override suspend fun emit(value: Map<DestinationId, T>) {
        state.emit(value.toMap())
    }

    override fun tryEmit(value: Map<DestinationId, T>): Boolean {
        return state.tryEmit(value.toMap())
    }
}
