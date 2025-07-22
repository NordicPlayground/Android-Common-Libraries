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

package no.nordicsemi.android.common.permissions.wifi.permissionState

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.common.permissions.wifi.WiFiPermissionNotAvailableReason
import no.nordicsemi.android.common.permissions.wifi.utils.LocalDataProvider
import no.nordicsemi.android.common.permissions.wifi.utils.PermissionUtils
import javax.inject.Inject
import javax.inject.Singleton

private const val REFRESH_LOCATION_PERMISSIONS =
    "no.nordicsemi.android.common.permissions.wifi.REFRESH_LOCATION_PERMISSIONS"

@Singleton
internal class LocationStateManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataProvider = LocalDataProvider(context)
    private val utils = PermissionUtils(context, dataProvider)

    @SuppressLint("WrongConstant")
    fun locationState() = callbackFlow {
        trySend(getLocationState())

        val locationStateChangeHandler = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                trySend(getLocationState())
            }
        }
        val filter = IntentFilter().apply {
            addAction(LocationManager.MODE_CHANGED_ACTION)
            addAction(REFRESH_LOCATION_PERMISSIONS)
        }
        ContextCompat.registerReceiver(
            context,
            locationStateChangeHandler,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )
        awaitClose {
            context.unregisterReceiver(locationStateChangeHandler)
        }
    }

    fun refreshPermission() {
        val intent = Intent(REFRESH_LOCATION_PERMISSIONS)
        context.sendBroadcast(intent)
    }

    fun markLocationPermissionRequested() {
        dataProvider.locationPermissionRequested = true
    }

    fun isLocationPermissionDeniedForever(context: Context): Boolean {
        return utils.isLocationPermissionDeniedForever(context)
    }

    private fun getLocationState(): WiFiPermissionState {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return when {
            !utils.isLocationPermissionGranted ->
                WiFiPermissionState.NotAvailable(WiFiPermissionNotAvailableReason.PERMISSION_REQUIRED)

            dataProvider.isLocationPermissionRequired && !LocationManagerCompat.isLocationEnabled(lm) ->
                WiFiPermissionState.NotAvailable(WiFiPermissionNotAvailableReason.DISABLED)

            else -> WiFiPermissionState.Available
        }
    }
}