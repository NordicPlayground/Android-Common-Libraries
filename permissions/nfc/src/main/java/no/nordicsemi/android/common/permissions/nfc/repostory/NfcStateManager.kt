package no.nordicsemi.android.common.permissions.nfc.repostory

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.common.permissions.nfc.utils.NfcPermissionUtils
import no.nordicsemi.android.common.permissions.nfc.utils.NfcNotAvailableReason
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
        context.registerReceiver(nfcStateChangeHandler, filter)
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