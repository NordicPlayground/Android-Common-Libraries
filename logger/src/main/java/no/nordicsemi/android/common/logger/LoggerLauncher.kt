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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package no.nordicsemi.android.common.logger

import android.content.Context
import android.content.Intent
import android.net.Uri
import no.nordicsemi.android.log.ILogSession
import no.nordicsemi.android.log.Logger

private const val LOGGER_PACKAGE_NAME = "no.nordicsemi.android.log"
private const val LOGGER_LINK = "https://play.google.com/store/apps/details?id=no.nordicsemi.android.log"

/**
 * Helper object responsible for launching nRF Logger app.
 */
object LoggerLauncher {

    /**
     * Opens the log session in nRF Logger app, or opens Google Play if the app is not installed.
     *
     * Use [Logger.newSession] to create a log session and log data to it.
     *
     * @param context the context.
     * @param logSession the log session to open.
     */
    fun launch(context: Context, logSession: ILogSession?) {
        val sessionUri = logSession?.sessionUri
        val packageManger = context.packageManager

        if (sessionUri != null && packageManger.getLaunchIntentForPackage(LOGGER_PACKAGE_NAME) != null) {
            open(context, sessionUri)
        } else try {
            open(context, Uri.parse(LOGGER_LINK))
        } catch (e: Exception) {
            e.printStackTrace() //Google Play not installed
        }
    }

    private fun open(context: Context, sessionUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, sessionUri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
