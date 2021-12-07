package no.nordicsemi.ui.scanner.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import no.nordicsemi.android.material.you.Card
import no.nordicsemi.android.material.you.Checkbox
import no.nordicsemi.android.material.you.CircularProgressIndicator
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.R
import no.nordicsemi.ui.scanner.scanner.repository.ErrorResult
import no.nordicsemi.ui.scanner.scanner.repository.LoadingResult
import no.nordicsemi.ui.scanner.scanner.repository.SuccessResult

@Composable
internal fun StringListDialog(config: StringListDialogConfig) {
    Dialog(onDismissRequest = { config.onResult(FlowCanceled) }) {
        StringListView(config)
    }
}

@Composable
internal fun StringListView(config: StringListDialogConfig) {
    Card(
        modifier = Modifier.height(300.dp),
        backgroundColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(10.dp),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = config.title ?: stringResource(id = R.string.dialog).toAnnotatedString(),
                    fontSize = 20.sp
                )
            }

            if (config.filterItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                config.filterItems.forEachIndexed { i, item ->
                    Row {
                        Checkbox(checked = item.isChecked, onCheckedChange = {
                            config.onFilterItemCheckChanged(i)
                        })
                        Spacer(modifier = Modifier.height(4.dp))
                        item.icon?.let {
                            Image(
                                painter = painterResource(id = it),
                                contentDescription = ""
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.text)
                    }
                }
            }

            if (config.filterItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .verticalScroll(rememberScrollState())
            ) {

                when (val result = config.result) {
                    is ErrorResult -> ErrorSection()
                    is LoadingResult -> LoadingSection()
                    is SuccessResult -> DevicesSection(result.value, config)
                }.exhaustive
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                TextButton(onClick = { config.onResult(FlowCanceled) }) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DevicesSection(
    items: List<DiscoveredBluetoothDevice>,
    config: StringListDialogConfig
) {
    items.forEach {
        Column(
            modifier = Modifier
                .clickable { config.onResult(ItemSelectedResult(it)) }
                .height(35.dp)
        ) {

            Row {
                config.leftIcon?.let {
                    Image(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        painter = painterResource(it),
                        contentDescription = "Content image"
                    )
                }
                Text(
                    text = it.displayName(),
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ErrorSection() {
    Text(
        text = stringResource(id = R.string.scan_failed),
        color = MaterialTheme.colorScheme.error
    )
}
