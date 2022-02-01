package no.nordicsemi.ui.scanner.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import no.nordicsemi.android.material.you.Card
import no.nordicsemi.android.material.you.CheckboxFallback
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
        modifier = Modifier.height(350.dp),
        backgroundColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(10.dp),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = config.title ?: stringResource(id = R.string.dialog).toAnnotatedString(),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                config.filterItems.forEachIndexed { i, item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CheckboxFallback(checked = item.isChecked, onCheckedChange = {
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
                Spacer(modifier = Modifier.height(8.dp))
            }

            Column(modifier = Modifier.fillMaxHeight(0.8f)) {
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
    LazyColumn {
        items(items.size) { i ->
            val device = items[i]
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { config.onResult(ItemSelectedResult(device)) }
                    .padding(8.dp),
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    config.leftIcon?.let {
                        Image(
                            painter = painterResource(it),
                            contentDescription = "Content image",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary),
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = CircleShape
                                )
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = device.displayName(),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = device.displayAddress(), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
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
