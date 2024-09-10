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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import no.nordicsemi.android.common.navigation.Navigator
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.test.simple.Hello
import no.nordicsemi.android.common.theme.NordicTheme
import no.nordicsemi.android.common.ui.view.NordicSliderDefaults
import no.nordicsemi.android.common.ui.view.PagerViewItem
import javax.inject.Inject

val BasicsPage = PagerViewItem("Basics") {
    val vm = hiltViewModel<BasicPageViewModel>()

    BasicViewsScreen(onOpenSimple = { vm.openSimple() },)
}

@HiltViewModel
class BasicPageViewModel @Inject constructor(
    private val navigator: Navigator,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    fun openSimple() {
        navigator.navigateTo(Hello, 1)
    }
}

@Composable
private fun BasicViewsScreen(
    onOpenSimple: () -> Unit,
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

        BasicViewsScreen()

        Cards()
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

        var sliderValue by rememberSaveable { mutableFloatStateOf(5.0f) }
        Text(
            text = stringResource(id = R.string.slider, sliderValue.toInt()),
        )
        Slider(
            value = sliderValue,
            valueRange = 0.0f..10.0f,
            onValueChange = { sliderValue = it },
            steps = 9,
            colors = NordicSliderDefaults.colors()
        )
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
            onOpenSimple = {},
        )
    }
}