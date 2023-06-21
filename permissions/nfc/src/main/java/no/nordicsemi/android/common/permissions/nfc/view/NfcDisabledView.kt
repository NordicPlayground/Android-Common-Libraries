package no.nordicsemi.android.common.permissions.nfc.view

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import no.nordicsemi.android.common.permissions.nfc.R
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.WarningView

@Composable
internal fun NfcDisabledView() {
    WarningView(
        painterResource = painterResource(id = R.drawable.nfc_variant_off),
        title = stringResource(id = R.string.nfc_disabled_title),
        hint = stringResource(id = R.string.nfc_disabled_info),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val context = LocalContext.current
        Button(onClick = { enableNfc(context) }) {
            Text(text = stringResource(id = R.string.action_enable))
        }
    }
}

private fun enableNfc(context: Context) {
    context.startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
}

@Preview
@Composable
private fun NfcDisabledView_Preview() {
    NordicTheme {
        NfcDisabledView()
    }
}
