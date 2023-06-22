package no.nordicsemi.android.common.permissions.nfc.utils

import android.content.Context
import android.content.pm.PackageManager
import android.nfc.NfcManager

internal class NfcPermissionUtils(
    private val context: Context,
) {
    val isNfcEnabled: Boolean
        get() = (context.getSystemService(Context.NFC_SERVICE) as NfcManager)
            .defaultAdapter
            .isEnabled

    val isNfcAvailable: Boolean
        get() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)
}