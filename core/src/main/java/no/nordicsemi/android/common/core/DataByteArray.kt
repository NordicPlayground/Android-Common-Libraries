package no.nordicsemi.android.common.core

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Wrapper around [ByteArray]. There is a problem with [ByteArray] when using inside data class,
 * because it requires to generate equals() and hashCode() methods. This class do this internally
 * so there is no need to do this again in data classes which uses [DataByteArray].
 *
 * @property value Original [ByteArray] value.
 */
//TODO check if [ByteArray] is  Parcelable.
@Parcelize
data class DataByteArray(val value: ByteArray = byteArrayOf()) : Parcelable {

    @IgnoredOnParcel
    val size = value.size

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

    override fun toString(): String {
        return value.toDisplayString()
    }

    fun copyOf(): DataByteArray {
        return DataByteArray(value.copyOf())
    }

    fun copyOfRange(fromIndex: Int, toIndex: Int): DataByteArray {
        return DataByteArray(value.copyOfRange(fromIndex, toIndex))
    }

    fun split(size: Int): List<DataByteArray> {
        return value.asList().chunked(size).map { DataByteArray(it.toByteArray()) }
    }

    companion object {

        fun from(vararg bytes: Byte): DataByteArray {
            return DataByteArray(bytes)
        }
    }
}
