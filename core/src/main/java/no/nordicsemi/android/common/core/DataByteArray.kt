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

    fun getChunk(offset: Int, mtu: Int): DataByteArray {
        val maxSize = mtu - 3
        val sizeLeft = this.size - offset
        return if (sizeLeft > 0) {
            if (sizeLeft > maxSize) {
                this.copyOfRange(offset, offset + maxSize)
            } else {
                this.copyOfRange(offset, this.size)
            }
        } else {
            DataByteArray()
        }
    }

    companion object {

        fun from(vararg bytes: Byte): DataByteArray {
            return DataByteArray(bytes)
        }
    }
}
