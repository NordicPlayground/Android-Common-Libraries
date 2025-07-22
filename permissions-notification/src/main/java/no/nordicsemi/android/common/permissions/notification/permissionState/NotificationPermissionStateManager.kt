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

package no.nordicsemi.android.common.permissions.notification.permissionState

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.common.permissions.notification.utils.LocalDataProvider
import no.nordicsemi.android.common.permissions.notification.utils.NotificationPermissionUtils
import javax.inject.Inject
import javax.inject.Singleton

private const val REFRESH_NOTIFICATION_PERMISSIONS =
    "no.nordicsemi.android.common.permissions.notification.REFRESH_NOTIFICATION_PERMISSIONS"

@Singleton
internal class NotificationStateManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataProvider = LocalDataProvider(context)
    private val utils = NotificationPermissionUtils(context, dataProvider)

    fun notificationState() = callbackFlow {
        trySend(getNotificationPermissionState())

        val notificationStateChangeHandler = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                trySend(getNotificationPermissionState())
            }
        }
        val filter = IntentFilter().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addAction(NotificationManager.ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED)
            }
            addAction(REFRESH_NOTIFICATION_PERMISSIONS)
        }

        ContextCompat.registerReceiver(
            context,
            notificationStateChangeHandler,
            filter,
            RECEIVER_EXPORTED
        )

        awaitClose {
            context.unregisterReceiver(notificationStateChangeHandler)
        }
    }

    fun refreshPermission() {
        val intent = Intent(REFRESH_NOTIFICATION_PERMISSIONS)
        context.sendBroadcast(intent)
    }

    fun markNotificationPermissionRequested() {
        dataProvider.notificationPermissionRequested = true
    }

    fun isNotificationPermissionDenied(context: Context): Boolean {
        return utils.isNotificationPermissionDenied(context)
    }

    private fun getNotificationPermissionState(): NotificationPermissionState {
        return when {
            !utils.isNotificationPermissionGranted && utils.isNotificationPermissionRequired ->
                NotificationPermissionState.NotAvailable(NotAvailableReason.PERMISSION_REQUIRED)

            utils.isNotificationPermissionDenied(context) ->
                NotificationPermissionState.NotAvailable(NotAvailableReason.DENIED)

            else -> NotificationPermissionState.Available
        }
    }
}
