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

package no.nordicsemi.android.common.permissions.nfc.repostory

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.common.permissions.nfc.NfcNotAvailableReason
import no.nordicsemi.android.common.permissions.nfc.utils.NfcPermissionUtils
import no.nordicsemi.android.common.permissions.nfc.utils.NfcPermissionState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NfcStateManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val utils = NfcPermissionUtils(context)

    fun nfcState() = callbackFlow {
        trySend(getNfcPermissionState())

        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)

        if (nfcAdapter == null) {
            trySend(NfcPermissionState.NotAvailable(NfcNotAvailableReason.NOT_AVAILABLE))
            channel.close()
            return@callbackFlow
        }

        trySend(getNfcPermissionState())

        val nfcStateChangeHandler = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                trySend(getNfcPermissionState())
            }
        }
        val filter = IntentFilter().apply {
            addAction(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)
        }
        ContextCompat.registerReceiver(context, nfcStateChangeHandler, filter, ContextCompat.RECEIVER_EXPORTED)
        awaitClose {
            context.unregisterReceiver(nfcStateChangeHandler)
        }
    }

    private fun getNfcPermissionState(): NfcPermissionState {
        return when {
            !utils.isNfcAvailable -> NfcPermissionState.NotAvailable(
                NfcNotAvailableReason.NOT_AVAILABLE
            )

            !utils.isNfcEnabled -> NfcPermissionState.NotAvailable(
                NfcNotAvailableReason.DISABLED
            )

            else -> NfcPermissionState.Available
        }
    }
}