package no.nordicsemi.android.common.permission.nfc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.common.permission.util.LocalDataProvider
import no.nordicsemi.android.common.permission.util.PermissionUtils
import no.nordicsemi.android.common.permission.util.StandardPermissionNotAvailableReason
import no.nordicsemi.android.common.permission.util.StandardPermissionState
import javax.inject.Inject
import javax.inject.Singleton

private const val REFRESH_PERMISSIONS =
    "no.nordicsemi.android.common.permission.REFRESH_NFC_PERMISSIONS"

@Singleton
internal class NfcStateManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataProvider = LocalDataProvider(context)
    private val utils = PermissionUtils(context, dataProvider)

    fun nfcState() = callbackFlow {
        trySend(getNfcPermissionState())

        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)

        if (nfcAdapter == null) {
            trySend(StandardPermissionState.NotAvailable(StandardPermissionNotAvailableReason.NOT_AVAILABLE))
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
            addAction(REFRESH_PERMISSIONS)
        }
        context.registerReceiver(nfcStateChangeHandler, filter)
        awaitClose {
            context.unregisterReceiver(nfcStateChangeHandler)
        }
    }

    private fun getNfcPermissionState(): StandardPermissionState {
        return when {
            !utils.isNfcAvailable -> StandardPermissionState.NotAvailable(
                StandardPermissionNotAvailableReason.NOT_AVAILABLE
            )

            !utils.isNfcEnabled -> StandardPermissionState.NotAvailable(
                StandardPermissionNotAvailableReason.DISABLED
            )

            else -> StandardPermissionState.Available
        }
    }
}