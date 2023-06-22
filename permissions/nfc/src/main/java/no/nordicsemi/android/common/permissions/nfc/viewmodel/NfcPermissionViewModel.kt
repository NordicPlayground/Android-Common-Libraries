package no.nordicsemi.android.common.permissions.nfc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import no.nordicsemi.android.common.permissions.nfc.repostory.NfcStateManager
import no.nordicsemi.android.common.permissions.nfc.utils.NfcNotAvailableReason
import no.nordicsemi.android.common.permissions.nfc.utils.NfcPermissionState
import javax.inject.Inject

class NfcPermissionViewModel @Inject internal constructor(
    nfcManager: NfcStateManager,
) : ViewModel() {

    val nfcPermission = nfcManager.nfcState()
        .stateIn(
            viewModelScope, SharingStarted.Lazily,
            NfcPermissionState.NotAvailable(NfcNotAvailableReason.NOT_AVAILABLE)
        )
}