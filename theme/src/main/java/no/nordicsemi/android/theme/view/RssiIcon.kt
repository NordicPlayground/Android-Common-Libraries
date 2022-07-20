package no.nordicsemi.android.theme.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import no.nordicsemi.android.theme.R

private const val MEDIUM_RSSI = -80
private const val MAX_RSSI = -60

@Composable
fun RssiIcon(rssi: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = getImageRes(rssi)),
            contentDescription = stringResource(id = R.string.cd_rssi)
        )
        Text(
            text = stringResource(id = R.string.dbm, rssi),
            style = MaterialTheme.typography.labelSmall
        )
    }

}

@DrawableRes
private fun getImageRes(rssi: Int): Int {
    return when  {
        rssi < MEDIUM_RSSI -> R.drawable.ic_signal_min
        rssi < MAX_RSSI -> R.drawable.ic_signal_medium
        else -> R.drawable.ic_signal_max
    }
}
