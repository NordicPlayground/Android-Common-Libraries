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

package no.nordicsemi.android.common.permissions.notification.view

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import no.nordicsemi.android.common.permissions.notification.viewmodel.NotificationPermissionViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun NotificationPermissionRequestView(
    content: @Composable (Boolean) -> Unit
) {
    val viewmodel: NotificationPermissionViewModel = hiltViewModel()
    // Notification permission
    // Android 13 (API level 33) and higher supports a runtime permission for sending non-exempt
    // (including Foreground Services (FGS)) notifications from an app.
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.POST_NOTIFICATIONS else null
    val context = LocalContext.current

    val notificationPermission = permission?.let { rememberPermissionState(it) }
    if (notificationPermission != null) {
        when (notificationPermission.status) {
            is PermissionStatus.Denied -> {
                // FCM SDK (and your app) cannot post notifications.
                LaunchedEffect(!notificationPermission.status.isGranted) {
                    viewmodel.markNotificationPermissionRequested()
                    notificationPermission.launchPermissionRequest()
                    if (!notificationPermission.status.isGranted) {
                        // Mark the permission as requested and denied.
                        viewmodel.isNotificationPermissionDenied(context)

                    }
                    viewmodel.refreshNotificationPermission()
                }
                content(notificationPermission.status.isGranted)
            }

            PermissionStatus.Granted -> {
                // FCM SDK (and your app) can post notifications.
                content(true)
            }
        }
    } else {
        // FCM SDK (and your app) can post notifications.
        viewmodel.refreshNotificationPermission()
        content(true)
    }

}