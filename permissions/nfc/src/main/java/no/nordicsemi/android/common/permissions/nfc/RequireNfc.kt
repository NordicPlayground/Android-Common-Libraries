package no.nordicsemi.android.common.permissions.nfc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.android.common.permissions.nfc.utils.NfcNotAvailableReason
import no.nordicsemi.android.common.permissions.nfc.utils.NfcPermissionState
import no.nordicsemi.android.common.permissions.nfc.view.NfcDisabledView
import no.nordicsemi.android.common.permissions.nfc.view.NfcNotAvailableView
import no.nordicsemi.android.common.permissions.nfc.viewmodel.NfcPermissionViewModel

@Composable
fun RequireNfc(
    onChanged: (Boolean) -> Unit = {},
    contentWithoutNfc: @Composable (NfcNotAvailableReason) -> Unit = {
        NoNfcView(reason = it)
    },
    content: @Composable () -> Unit,
) {
    val viewModel: NfcPermissionViewModel = hiltViewModel()
    val state by viewModel.nfcPermission.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        onChanged(state is NfcPermissionState.Available)
    }

    when (val s = state) {
        NfcPermissionState.Available -> content()
        is NfcPermissionState.NotAvailable -> contentWithoutNfc(s.reason)
    }
}

@Composable
private fun NoNfcView(
    reason: NfcNotAvailableReason,
) {
    when (reason) {
        NfcNotAvailableReason.NOT_AVAILABLE -> NfcNotAvailableView()
        NfcNotAvailableReason.DISABLED -> NfcDisabledView()
    }
}