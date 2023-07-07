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
import no.nordicsemi.android.log.Logger
import no.nordicsemi.android.log.annotation.LogLevel

/**
 * Creates a new instance of the logger
 *
 * @param context the application context.
 * @param profile The profile name. This name will be shown in nRF Logger next to the app's name,
 *                e.g. nRF Toolbox "Proximity" in the navigation menu.
 * @param key The key are use to group the logs. Usually, the key is the device MAC address.
 * @param name An optional identifier for the log session, usually a device name.
 */
class NordicLogger constructor(
    private val context: Context,
    profile: String?,
    key: String,
    name: String?,
) : BlekLoggerAndLauncher {
    private val logSession = Logger.newSession(context, profile, key, name)

    /**
     * Logs the given message with the given log level.
     *
     * If nRF Logger is not installed, this method does nothing.
     */
    override fun log(@LogLevel priority: Int, log: String) {
        Logger.log(logSession, priority, log)
    }

    override fun launch() {
        LoggerLauncher.launch(context, logSession?.sessionsUri)
    }
}

