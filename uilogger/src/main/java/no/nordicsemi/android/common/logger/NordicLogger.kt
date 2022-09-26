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

package no.nordicsemi.android.common.logger

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import no.nordicsemi.android.log.Logger
import no.nordicsemi.android.log.annotation.LogLevel

private const val LOGGER_PACKAGE_NAME = "no.nordicsemi.android.log"
private const val LOGGER_LINK = "https://play.google.com/store/apps/details?id=no.nordicsemi.android.log"

/**
 * Creates a new instance of the logger
 *
 * @param context the application context.
 * @param profile The profile name. This name will be shown in nRF Logger next to the app's name,
 *                e.g. nRF Toolbox "Proximity" in the navigation menu.
 * @param key The key are use to group the logs. Usually, the key is the device MAC address.
 * @param name An optional identifier for the log session, usually a device name.
 */
class NordicLogger @AssistedInject constructor(
    @ApplicationContext val context: Context,
    @Assisted("profile") profile: String?,
    @Assisted("key") key: String,
    @Assisted("name") name: String?,
) {
    private val logSession = Logger.newSession(context, profile, key, name)

    /**
     * Logs the given message with the given log level.
     *
     * If nRF Logger is not installed, this method does nothing.
     */
    fun log(@LogLevel level: Int, message: String) {
        Logger.log(logSession, level, message)
    }

    companion object {

        /**
         * Opens the log session in nRF Logger app, or opens Google Play if the app is not installed.
         */
        fun launch(context: Context, logger: NordicLogger? = null) {
            val packageManger = context.packageManager

            // Make sure nRF Logger is installed.
            val intent = packageManger.getLaunchIntentForPackage(LOGGER_PACKAGE_NAME) ?: run {
                with (Intent(Intent.ACTION_VIEW, Uri.parse(LOGGER_LINK))) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(this)
                }
                return@launch
            }

            // Start nRF Logger and show the log session, or the main screen if session does not exist.
            with (logger?.logSession?.sessionUri?.let { Intent(Intent.ACTION_VIEW, it) } ?: intent) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(this)
            }
        }

    }
}