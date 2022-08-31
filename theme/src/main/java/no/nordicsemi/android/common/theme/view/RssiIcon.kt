package no.nordicsemi.android.common.theme.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NetworkWifi
import androidx.compose.material.icons.filled.NetworkWifi1Bar
import androidx.compose.material.icons.filled.NetworkWifi3Bar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import no.nordicsemi.android.common.theme.R

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
    return when {
        rssi < MEDIUM_RSSI -> R.drawable.ic_signal_min
        rssi < MAX_RSSI -> R.drawable.ic_signal_medium
        else -> R.drawable.ic_signal_max
    }
}

fun getWiFiRes(rssi: Int): ImageVector {
    return when {
        rssi < MEDIUM_RSSI -> Icons.Default.NetworkWifi1Bar
        rssi < MAX_RSSI -> Icons.Default.NetworkWifi3Bar
        else -> Icons.Default.NetworkWifi
    }
}
