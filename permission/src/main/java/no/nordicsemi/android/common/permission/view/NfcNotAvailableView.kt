package no.nordicsemi.android.common.permission.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import no.nordicsemi.android.common.permission.R
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.WarningView


@Composable
internal fun NfcNotAvailableView() {
    WarningView(
        painterResource = painterResource(id = R.drawable.nfc_variant_off),
        title = stringResource(id = R.string.nf_not_available_title),
        hint = stringResource(id = R.string.nfc_not_available_info),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    )
}

@Preview
@Composable
private fun NfcNotAvailableView_Preview() {
    NordicTheme {
        NfcNotAvailableView()
    }
}
