package no.nordicsemi.ui.scanner.bonding.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.ui.scanner.R

@Composable
internal fun BondingSuccessView() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.bonding_success),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun BondingSuccessViewPreview() {
    BondingSuccessView()
}
