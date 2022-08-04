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
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val LOGGER_PACKAGE_NAME = "no.nordicsemi.android.log"
private const val LOGGER_LINK = "https://play.google.com/store/apps/details?id=no.nordicsemi.android.log"

class LoggerAppRunner @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    fun runLogger() {
        val packageManger = context.packageManager

        val intent = packageManger.getLaunchIntentForPackage(LOGGER_PACKAGE_NAME)
        if (intent != null) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } else {
            val launchIntent = Intent(Intent.ACTION_VIEW, Uri.parse(LOGGER_LINK))
            launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(launchIntent)
        }
    }

    fun runLogger(uri: Uri?) {
        val packageManger = context.packageManager

        val intent = packageManger.getLaunchIntentForPackage(LOGGER_PACKAGE_NAME)

        val targetUri = if (intent != null && uri != null) {
            uri
        } else {
            Uri.parse(LOGGER_LINK)
        }
        val launchIntent = Intent(Intent.ACTION_VIEW, targetUri)
        launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(launchIntent)
    }
}
