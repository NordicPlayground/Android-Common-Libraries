package no.nordicsemi.ui.scanner.bonding.viewmodel

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import no.nordicsemi.ui.scanner.bonding.repository.BondingStateObserver
import no.nordicsemi.ui.scanner.bonding.repository.BondingState
import no.nordicsemi.ui.scanner.bonding.repository.getBondingState

class BondingViewModel(
    private val device: BluetoothDevice,
    private val bondingStateObserver: BondingStateObserver
) : ViewModel() {

    val state = MutableStateFlow(device.getBondingState())

    init {
        bondingStateObserver.events.onEach { event ->
            event.device?.let {
                if (it == device) {
                    state.tryEmit(event.bondState)
                } else {
                    state.tryEmit(BondingState.NONE)
                }
            } ?: state.tryEmit(event.bondState)
        }.launchIn(viewModelScope)
        bondingStateObserver.startObserving()
    }

    fun bondDevice() {
        device.createBond()
    }

    override fun onCleared() {
        super.onCleared()
        bondingStateObserver.stopObserving()
    }
}
