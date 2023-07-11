package no.nordicsemi.android.common.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataByteArray(val value: ByteArray) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataByteArray

        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int {
        return value.contentHashCode()
    }

}
