/*
 * Copyright (c) 2024, Nordic Semiconductor
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

package no.nordicsemi.android.common.permissions.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.android.common.permissions.notification.utils.NotAvailableReason
import no.nordicsemi.android.common.permissions.notification.utils.NotificationPermissionState
import no.nordicsemi.android.common.permissions.notification.view.NotificationPermissionRequestView
import no.nordicsemi.android.common.permissions.notification.viewmodel.NotificationPermissionViewModel

/**
 * A wrapper for composables that show notifications to the user.
 *
 * This composable will request the notification permission if needed.
 *
 * Content is displayed in any case and the state of the permission is given as a parameter.
 *
 * ### Example:
 * ```kotlin
 * RequestNotificationPermission(
 *    onChanged = { granted ->
 *    // Handle notification state change
 *    }
 * ) { canShowNotifications ->
 *    // Your content
 * }
 * ```
 *
 * @param onChanged A callback that will be called when the state of the notification changes.
 * @param content The content to display. The parameter is `true` if the notification permission
 * is granted or not required, `false` otherwise.
 */
@Composable
fun RequestNotificationPermission(
    onChanged: (Boolean) -> Unit = {},
    content: @Composable (Boolean?) -> Unit,
) {
    val viewModel = hiltViewModel<NotificationPermissionViewModel>()
    val state by viewModel.notificationState.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        onChanged(state is NotificationPermissionState.Available)
    }

    when (val s = state) {
        NotificationPermissionState.Available -> content(true)
        is NotificationPermissionState.NotAvailable -> {
            when (s.reason) {
                NotAvailableReason.PERMISSION_REQUIRED -> {
                    // Request permission
                    NotificationPermissionRequestView(content)
                }

                NotAvailableReason.DENIED ->  content(false)
            }
        }
    }
}
