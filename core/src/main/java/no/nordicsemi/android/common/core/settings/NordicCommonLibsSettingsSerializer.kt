package no.nordicsemi.android.common.core.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object NordicCommonLibsSettingsSerializer : Serializer<NordicCommonLibsSettings> {
    override val defaultValue: NordicCommonLibsSettings =
        NordicCommonLibsSettings.newBuilder()
            .setLocationPermissionRequested(false)
            .setBluetoothPermissionRequested(false)
            .setNotificationPermissionRequested(false)
            .setWifiPermissionRequested(false)
            .build()

    override suspend fun readFrom(input: InputStream): NordicCommonLibsSettings {
        try {
            return NordicCommonLibsSettings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: NordicCommonLibsSettings, output: OutputStream) =
        t.writeTo(output)
}