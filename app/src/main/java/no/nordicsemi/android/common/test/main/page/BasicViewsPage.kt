/*
 * Copyright (c) 2022, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.common.test.main.page

import android.annotation.SuppressLint
import android.os.ParcelUuid
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import no.nordicsemi.android.common.navigation.NavigationResult
import no.nordicsemi.android.common.navigation.Navigator
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.test.scanner.Scanner
import no.nordicsemi.android.common.test.simple.Hello
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.theme.view.PagerViewItem
import no.nordicsemi.android.common.theme.view.RssiIcon
import no.nordicsemi.android.common.ui.scanner.main.DeviceListItem
import no.nordicsemi.android.kotlin.ble.core.ServerDevice
import java.util.*
import javax.inject.Inject

val BasicsPage = PagerViewItem("Basics") {
    val vm = hiltViewModel<BasicPageViewModel>()
    val device by vm.device.collectAsStateWithLifecycle()

    BasicViewsScreen(
        device = device?.let { DeviceInfo(it) },
        onOpenSimple = { vm.openSimple() },
        onOpenScanner = { vm.openScanner() },
    )
}

private const val DEVICE_KEY = "device"

@HiltViewModel
class BasicPageViewModel @Inject constructor(
    private val navigator: Navigator,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    // Initialize the selected device from the saved state handle.
    val device = savedStateHandle.getStateFlow<ServerDevice?>(DEVICE_KEY, null)

    init {
        navigator.resultFrom(Scanner)
            // Filter out results of cancelled navigation.
            .mapNotNull { it as? NavigationResult.Success }
            .map { it.value }
            // Save the result in SavedStateHandle.
            .onEach { savedStateHandle[DEVICE_KEY] = it }
            // And finally, launch the flow in the ViewModelScope.
            .launchIn(viewModelScope)
    }

    fun openScanner() {
        // This is Mesh Proxy Service UUID
        navigator.navigateTo(Scanner, ParcelUuid(UUID.fromString("00001828-0000-1000-8000-00805F9B34FB")))
    }

    fun openSimple() {
        navigator.navigateTo(Hello, 1)
    }
}

@SuppressLint("MissingPermission")
data class DeviceInfo(
    private val device: ServerDevice?
) {
    val name = device?.name ?: "Unknown"
    val address = device?.address ?: "Unknown"
}

@Composable
private fun BasicViewsScreen(
    device: DeviceInfo?,
    onOpenSimple: () -> Unit,
    onOpenScanner: () -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = onOpenSimple
            ) {
                Text(text = stringResource(id = R.string.action_simple))
            }
        }

        Button(
            onClick = onOpenScanner
        ) {
            Text(text = stringResource(id = R.string.action_scanner))
        }

        device?.let {
            Device(
                deviceName = it.name,
                deviceAddress = it.address
            )
        }

        BasicViewsScreen()

        Cards()
    }
}

@Composable
private fun Device(
    deviceName: String,
    deviceAddress: String
) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(10.dp))
        .clickable { }
        .padding(8.dp)
    ) {
        DeviceListItem(
            name = deviceName,
            address = deviceAddress,
        )
    }
}

@Composable
private fun Buttons() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(onClick = { }) {
            Text(text = stringResource(id = R.string.action_no_op))
        }

        Button(
            onClick = { },
            enabled = false,
        ) {
            Text(text = stringResource(id = R.string.action_disabled))
        }

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
private fun BasicViewsScreen() {
    Column {
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

@Preview
@Composable
private fun ContentPreview() {
    NordicTheme {
        BasicViewsScreen(
            device = null,
            onOpenSimple = {},
            onOpenScanner = {},
        )
    }
}