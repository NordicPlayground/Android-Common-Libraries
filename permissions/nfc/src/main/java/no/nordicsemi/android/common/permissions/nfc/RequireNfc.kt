package no.nordicsemi.android.common.permissions.nfc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.android.common.permissions.nfc.utils.StandardPermissionNotAvailableReason
import no.nordicsemi.android.common.permissions.nfc.utils.StandardPermissionState
import no.nordicsemi.android.common.permissions.nfc.view.NfcDisabledView
import no.nordicsemi.android.common.permissions.nfc.view.NfcNotAvailableView
import no.nordicsemi.android.common.permissions.nfc.viewmodel.NfcPermissionViewModel

@Composable
fun RequireNfc(
    onChanged: (Boolean) -> Unit = {},
    contentWithoutNfc: @Composable (StandardPermissionNotAvailableReason) -> Unit = {
        NoNfcView(reason = it)
    },
    content: @Composable () -> Unit,
) {
    val viewModel: NfcPermissionViewModel = hiltViewModel()
    val state by viewModel.nfcPermission.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        onChanged(state is StandardPermissionState.Available)
    }

    when (val s = state) {
        StandardPermissionState.Available -> content()
        is StandardPermissionState.NotAvailable -> contentWithoutNfc(s.reason)
    }
}

@Composable
private fun NoNfcView(
    reason: StandardPermissionNotAvailableReason,
) {
    when (reason) {
        StandardPermissionNotAvailableReason.NOT_AVAILABLE -> NfcNotAvailableView()
        StandardPermissionNotAvailableReason.DISABLED -> NfcDisabledView()
    }
}