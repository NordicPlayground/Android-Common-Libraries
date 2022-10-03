package no.nordicsemi.android.common.test.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.logger.view.LoggerAppBarIcon
import no.nordicsemi.android.common.navigation.ComposeDestination
import no.nordicsemi.android.common.navigation.ComposeDestinations
import no.nordicsemi.android.common.navigation.DestinationId
import no.nordicsemi.android.common.navigation.NavigationManager
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.*

val Main = DestinationId("main")

val mainDestinations = ComposeDestinations(listOf(
    ComposeDestination(Main) { navigationManager ->
        MainScreen(navigationManager)
    },
))

@Composable
fun MainScreen(
    navigationManager: NavigationManager,
) {
    Column {
        NordicAppBar(
            text = stringResource(id = R.string.title_main),
            actions = {
                val context = LocalContext.current
                LoggerAppBarIcon(
                    onClick = {
                        Toast.makeText(context, "Logger clicked", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
        Content(
            onOpenScanner = {}
        )
    }
}

@Composable
private fun Content(
    onOpenScanner: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onOpenScanner
        ) {
            Text(text = stringResource(id = R.string.action_scanner))
        }

        Spacer(modifier = Modifier.height(16.dp))

        BasicViews()

        Spacer(modifier = Modifier.height(16.dp))

        Cards()

        Spacer(modifier = Modifier.height(16.dp))

        Wizard()
    }
}

@Composable
private fun Buttons() {
    Row {
        Button(onClick = { }) {
            Text(text = stringResource(id = R.string.action_no_opr))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = { },
            enabled = false,
        ) {
            Text(text = stringResource(id = R.string.action_disabled))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text(text = stringResource(id = R.string.action_destroy))
        }
    }
}

@Composable
private fun OtherWidgets() {
    Column {
        var checkedCheckbox by rememberSaveable { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { checkedCheckbox = !checkedCheckbox },
        ) {

            Text(
                text = stringResource(id = R.string.option),
                modifier = Modifier.weight(1.0f),
            )

            Checkbox(checked = checkedCheckbox, onCheckedChange = { checkedCheckbox = it })
        }

        var checkedSwitch by rememberSaveable { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { checkedSwitch = !checkedSwitch },
        ) {

            Text(
                text = stringResource(id = R.string.option),
                modifier = Modifier.weight(1.0f),
            )

            Switch(checked = checkedSwitch, onCheckedChange = { checkedSwitch = it })
        }
    }
}

@Composable
private fun BasicViews() {
    Column {
        // Text samples
        Text(text = stringResource(id = R.string.text_default))

        Spacer(modifier = Modifier.height(16.dp))

        Buttons()

        Spacer(modifier = Modifier.height(16.dp))

        OtherWidgets()
    }
}

@Composable
private fun Cards() {
    Column {
        OutlinedCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(id = R.string.text_card_outlined))

                Spacer(modifier = Modifier.height(16.dp))

                Buttons()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(id = R.string.text_card))

                Spacer(modifier = Modifier.height(16.dp))

                Buttons()
            }
        }
    }
}

@Composable
private fun Wizard() {
    OutlinedCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            WizardStepComponent(
                icon = Icons.Default.Warning,
                title = stringResource(id = R.string.wizard_step_completed),
                state = WizardStepState.COMPLETED,
                decor = WizardStepAction.Action(
                    text = stringResource(id = R.string.action_no_opr),
                    onClick = { }
                ),
            ) {
                Text(text = stringResource(id = R.string.wizard_text_completed))
            }
            WizardStepComponent(
                icon = Icons.Default.AccountBox,
                title = stringResource(id = R.string.wizard_step_current),
                state = WizardStepState.CURRENT,
                decor = WizardStepAction.Action(
                    text = stringResource(id = R.string.action_no_opr),
                    onClick = { }
                ),
            ) {
                Text(text = stringResource(id = R.string.wizard_text_current))
            }
            WizardStepComponent(
                icon = Icons.Default.AccountCircle,
                title = stringResource(id = R.string.wizard_step_in_progress),
                state = WizardStepState.CURRENT,
                decor = WizardStepAction.ProgressIndicator,
            ) {
                ProgressItem(
                    text = stringResource(id = R.string.wizard_text_completed),
                    status = ProgressItemStatus.SUCCESS,
                )
                ProgressItem(
                    text = stringResource(id = R.string.wizard_task_in_progress),
                    status = ProgressItemStatus.WORKING,
                ) {
                    LinearProgressIndicator(
                        progress = 0.3f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "30%",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
                ProgressItem(
                    text = stringResource(id = R.string.wizard_task_next),
                    status = ProgressItemStatus.DISABLED,
                )
                ProgressItem(
                    text = stringResource(id = R.string.wizard_task_error),
                    status = ProgressItemStatus.ERROR,
                )
            }
            WizardStepComponent(
                icon = Icons.Default.Build,
                title = stringResource(id = R.string.wizard_step_inactive),
                state = WizardStepState.INACTIVE,
                decor = WizardStepAction.Action(
                    text = stringResource(id = R.string.action_no_opr),
                    dangerous = true,
                    onClick = { }
                ),
            ) {
                Text(text = stringResource(id = R.string.wizard_text_inactive))
            }
        }
    }
}

@Preview
@Composable
fun ContentPreview() {
    NordicTheme {
        Content(
            onOpenScanner = {}
        )
    }
}