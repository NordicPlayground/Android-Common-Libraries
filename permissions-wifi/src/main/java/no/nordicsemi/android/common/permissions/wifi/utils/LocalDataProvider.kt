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

package no.nordicsemi.android.common.permissions.wifi.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.nordicsemi.android.common.core.ApplicationScope
import no.nordicsemi.android.common.core.settings.NordicCommonLibsSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LocalDataProvider @Inject constructor(
    private val repo: NordicCommonLibsSettingsRepository
) {
    private val _locationPermissionRequested = repo.nordicCommonLibsSettings
        .map{ settings -> settings.locationPermissionRequested }
        .stateIn(
            ApplicationScope,
            SharingStarted.Lazily,
            false
        )
    private val _wifiPermissionRequested = repo.nordicCommonLibsSettings
        .map { settings -> settings.wifiPermissionRequested }
        .stateIn(
            ApplicationScope,
            SharingStarted.Lazily,
            false
        )
    /**
     * The first time an app requests a permission there is no 'Don't ask again' checkbox and
     * [ActivityCompat.shouldShowRequestPermissionRationale] returns false.
     * This situation is similar to a permission being denied forever, so to distinguish both cases
     * a flag needs to be saved.
     */
    var locationPermissionRequested: Boolean
        get() = _locationPermissionRequested.value
        set(value) {
            ApplicationScope.launch(Dispatchers.IO) {
                repo.updateLocationPermissionRequested(value)
            }
        }

    /**
     * The first time an app requests a permission there is no 'Don't ask again' checkbox and
     * [ActivityCompat.shouldShowRequestPermissionRationale] returns false.
     * This situation is similar to a permission being denied forever, so to distinguish both cases
     * a flag needs to be saved.
     */
    var wifiPermissionRequested: Boolean
        get() = _wifiPermissionRequested.value
        set(value) {
            ApplicationScope.launch(Dispatchers.IO) {
                repo.updateWifiPermissionRequested(value)
            }
        }

    val isLocationPermissionRequired: Boolean
        /**
         * Location enabled is required on phones running Android 6 - 11
         * (for example on Nexus and Pixel devices). Initially, Samsung phones didn't require it,
         * but that has been fixed for those phones in Android 9.
         * Several Wi-Fi APIs require the ACCESS_FINE_LOCATION permission,
         * even when your app targets Android 13 or higher.
         *
         * @return False if it is known that location is not required, true otherwise.
         */
        get() = isMarshmallowOrAbove

    val isMarshmallowOrAbove: Boolean
        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    val isTiramisuOrAbove: Boolean
        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    init {
        ApplicationScope.launch { repo.fetchInitialSettings() }
    }
}
