package no.nordicsemi.android.common.test.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.navigation.createSimpleDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.navigation.viewmodel.SimpleNavigationViewModel
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.test.simple.Hello
import no.nordicsemi.android.common.test.simple.HelloDestination
import no.nordicsemi.android.common.theme.NordicTheme

val Second = createSimpleDestination("second")

private val SecondDestination = defineDestination(Second) {
    val vm: SimpleNavigationViewModel = hiltViewModel()

    SecondScreen(
        onClick = { vm.navigateTo(Hello, 2)}
    )
}

val SecondDestinations = SecondDestination + HelloDestination

@Composable
private fun SecondScreen(
    onClick: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "This tab allows to navigate to a different destination in scope of the same tab.\nAlso, a parameter \"2\" is passed.",
            textAlign = TextAlign.Center,
        )

        Button(onClick = onClick) {
            Text(stringResource(id = R.string.action_simple))
        }
    }
}

@Preview
@Composable
private fun SecondScreenPreview() {
    NordicTheme {
        SecondScreen()
    }
}