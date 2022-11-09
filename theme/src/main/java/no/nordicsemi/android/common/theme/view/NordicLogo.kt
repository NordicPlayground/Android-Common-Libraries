package no.nordicsemi.android.common.theme.view

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import no.nordicsemi.android.common.theme.R

@Composable
fun NordicLogo(
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.nordic_logo),
        contentDescription = stringResource(id = R.string.nordic_logo_accessibility)
    )
}