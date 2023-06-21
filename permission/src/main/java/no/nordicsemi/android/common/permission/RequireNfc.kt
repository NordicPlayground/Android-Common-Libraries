package no.nordicsemi.android.common.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.android.common.permission.util.StandardPermissionNotAvailableReason
import no.nordicsemi.android.common.permission.util.StandardPermissionState
import no.nordicsemi.android.common.permission.view.NfcDisabledView
import no.nordicsemi.android.common.permission.view.NfcNotAvailableView
import no.nordicsemi.android.common.permission.viewmodel.PermissionViewModel

@Composable
fun RequireNfc(
    onChanged: (Boolean) -> Unit = {},
    contentWithoutNfc: @Composable (StandardPermissionNotAvailableReason) -> Unit = {
        NoNfcView(reason = it)
    },
    content: @Composable () -> Unit,
) {
    val viewModel = hiltViewModel<PermissionViewModel>()
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
