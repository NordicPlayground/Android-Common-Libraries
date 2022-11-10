package no.nordicsemi.android.common.test.simple

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.common.navigation.createDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.navigation.defineDialogDestination
import no.nordicsemi.android.common.navigation.viewmodel.SimpleNavigationViewModel
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.theme.NordicTheme

/**
 * This is an example of a simple destination.
 *
 * A simple destination does not take any parameters and does not return any result.
 */
val Hello = createDestination<Int, Unit>("hello")

private val HelloDestination = defineDestination(Hello) {
    val vm: SimpleNavigationViewModel = hiltViewModel()
    val param = vm.parameterOf(Hello)

    HelloScreen(
        param = param,
        onShowDialog = { vm.navigateTo(HelloDialog, "Hello World!") },
        modifier = Modifier.fillMaxWidth(),
    )
}

val HelloDialog = createDestination<String, Unit>("hello-dialog")

private val HelloDialogDestination = defineDialogDestination(HelloDialog) {
    val vm: SimpleNavigationViewModel = hiltViewModel()
    val param = vm.parameterOf(HelloDialog)

    // This should not be AlertDialog, but AlertDialogContent. This is already wrapped in Dialog.
    // Unfortunately, the AlertDialogContent is internal.
    AlertDialog(
        onDismissRequest = { vm.navigateUp() },
        title = { Text(text = param) },
        text = { Text(text = "This is a dialog.") },
        confirmButton = {
            Button(onClick = { vm.navigateUp() }) {
                Text(text = "OK")
            }
        },
    )
}

val HelloDestinations = HelloDestination + HelloDialogDestination

@Composable
private fun HelloScreen(
    param: Int,
    onShowDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(id = R.string.hello))

            Text(text = stringResource(id = R.string.hello_param, param))

            Button(onClick = onShowDialog) {
                Text(text = stringResource(id = R.string.action_dialog))
            }
        }
    }
}

@Preview
@Composable
private fun SimpleContentPreview() {
    NordicTheme {
        HelloScreen(
            param = 0,
            onShowDialog = {},
        )
    }
}