/*
 * Copyright (c) 2023, Nordic Semiconductor
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

@file:Suppress("unused")

package no.nordicsemi.android.common.permissions.internet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.android.common.permissions.internet.util.InternetPermissionState
import no.nordicsemi.android.common.permissions.internet.view.InternetNotAvailableView
import no.nordicsemi.android.common.permissions.internet.viewmodel.InternetPermissionViewModel

/**
 * A wrapper for composables that require Internet.
 *
 * ### Example:
 * ```kotlin
 * RequireBluetooth(
 *     onChanged = { enabled ->
 *         // Handle Internet state change
 *     }
 * ) {
 *     // Your content
 * }
 * ```
 *
 * @param onChanged A callback that will be called when the state of the internet changes.
 * @param contentWithoutInternet A composable that will be displayed when internet is not available.
 * @param content A composable that will be displayed when internet is available.
 */
@Composable
fun RequireInternet(
    onChanged: (Boolean) -> Unit = {},
    contentWithoutInternet: @Composable () -> Unit = { InternetNotAvailableView() },
    content: @Composable () -> Unit
) {
    val viewModel = hiltViewModel<InternetPermissionViewModel>()
    val state by viewModel.internetPermission.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        onChanged(state is InternetPermissionState.Available)
    }

    when (state) {
        InternetPermissionState.Available -> content()
        else -> contentWithoutInternet()
    }
}