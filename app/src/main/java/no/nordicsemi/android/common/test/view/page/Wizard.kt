package no.nordicsemi.android.common.test.view.page

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.*

val Wizard = PagerViewItem("Wizard") {
    Wizard()
}

@Composable
private fun Wizard() {
    OutlinedCard(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
    ) {
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

                val infiniteTransition = rememberInfiniteTransition()
                val progress by infiniteTransition.animateFloat(
                    initialValue = 0.0f,
                    targetValue = 1.0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(10000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )

                ProgressItem(
                    text = stringResource(id = R.string.wizard_task_in_progress),
                    status = ProgressItemStatus.WORKING,
                ) {
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "%.1f%%".format(progress * 100),
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
private fun ContentPreview() {
    NordicTheme {
        Wizard()
    }
}