package no.nordicsemi.android.common.test.simple

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.navigation.createSimpleDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.navigation.viewmodel.SimpleNavigationViewModel
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.NordicAppBar

/**
 * This is an example of a simple destination.
 *
 * A simple destination does not take any parameters and does not return any result.
 */
val Hello = createSimpleDestination("hello")

val HelloDestination = defineDestination(Hello) {
    val vm: SimpleNavigationViewModel = hiltViewModel()

    HelloScreen(
        modifier = Modifier.fillMaxWidth(),
        onNavigateUp = { vm.navigateUp() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HelloScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        NordicAppBar(
            text = stringResource(id = R.string.hello),
            onNavigationButtonClick = onNavigateUp,
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(id = R.string.hello))

            Button(onClick = onNavigateUp) {
                Text(text = stringResource(id = R.string.action_dialog))
            }
        }
    }
}

@Preview
@Composable
private fun SimpleContentPreview() {
    NordicTheme {
        HelloScreen(onNavigateUp = {})
    }
}