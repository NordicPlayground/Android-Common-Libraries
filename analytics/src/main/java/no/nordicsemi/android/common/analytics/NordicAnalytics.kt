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

package no.nordicsemi.android.common.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.Size
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import no.nordicsemi.android.common.analytics.repository.AnalyticsPermissionRepository
import javax.inject.Inject
import javax.inject.Singleton

private const val LOG_TAG = "ANALYTICS"

/**
 * This class is responsible for logging events to Firebase Analytics.
 *
 * Use Hilt injection to get an instance of this class.
 */
@Singleton
class NordicAnalytics @Inject internal constructor(
    @ApplicationContext private val context: Context,
    private val repository: AnalyticsPermissionRepository,
) {
    /**
     * A flow that emits the current Analytics permission data.
     *
     * @see AnalyticsPermissionData
     */
    val permissionData = repository.permissionData

    private val firebase by lazy { FirebaseAnalytics.getInstance(context) }

    /**
     * Logs an event to Firebase Analytics, if the user has granted permission.
     *
     * @param name The name of the event. Should be between 1 and 40 characters long.
     * @param params Optional parameters to be sent with the event.
     */
    fun logEvent(@Size(min = 1L, max = 40L) name: String, params: Bundle? = null) {
        runBlocking {
            repository.permissionData.firstOrNull()
                ?.takeIf { it.isPermissionGranted }
                ?.runCatching {
                    Log.d(LOG_TAG, "name: $name, params: $params")
                    firebase.logEvent(name, params)
                }
        }
    }

    /**
     * Sets whether analytics collection is enabled or disabled.
     *
     * @param isEnabled True to enable analytics collection, false to disable it.
     */
    suspend fun setAnalyticsEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            repository.onPermissionGranted()
        } else {
            repository.onPermissionDenied()
        }
        firebase.setAnalyticsCollectionEnabled(isEnabled)
    }
}
