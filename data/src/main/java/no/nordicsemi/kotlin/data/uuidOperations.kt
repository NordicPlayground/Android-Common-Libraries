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

@file:Suppress("unused")

package no.nordicsemi.kotlin.data

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID

/**
 * Converts a 128-bit UUID to a byte array.
 * @param order The byte order, default is [ByteOrder.BIG_ENDIAN].
 */
// TODO: 2021-08-26: Add support for 16-bit and 32-bit UUIDs.
fun UUID.toByteArray(shortenIfPossible: Boolean, order: ByteOrder = ByteOrder.BIG_ENDIAN): ByteArray =
    ByteBuffer.wrap(ByteArray(16))
        .order(order)
        .apply {
            if (order == ByteOrder.BIG_ENDIAN) {
                putLong(mostSignificantBits)
                putLong(leastSignificantBits)
            } else {
                putLong(leastSignificantBits)
                putLong(mostSignificantBits)
            }
        }
        .array()

/**
 * Converts a byte array to a 128-bit UUID.
 * @param offset The index to start from.
 * @param order The byte order, default is [ByteOrder.BIG_ENDIAN].
 * @return UUID
 * @throws IllegalArgumentException If the byte array is shorter than 16 bytes long.
 */
fun ByteArray.getUuid(offset: Int, order: ByteOrder = ByteOrder.BIG_ENDIAN): UUID {
    require(offset >= 0 && size >= offset + 16) {
        throw IndexOutOfBoundsException("Cannot return a UUID from an array of size $size from offset $offset")
    }
    val buffer = ByteBuffer.wrap(this).order(order).position(offset)
    return when (order) {
        ByteOrder.BIG_ENDIAN -> UUID(buffer.long, buffer.long)
        else -> {
            val leastSignificantBits = buffer.long
            val mostSignificantBits = buffer.long
            UUID(mostSignificantBits, leastSignificantBits)
        }
    }
}

/**
 * Converts a byte array to a 128-bit UUID.
 * @param order The byte order, default is [ByteOrder.BIG_ENDIAN].
 * @return UUID
 * @throws IllegalArgumentException If the byte array is not 16 bytes long.
 */
fun ByteArray.toUuid(order: ByteOrder = ByteOrder.BIG_ENDIAN): UUID {
    require(size == 16) {
        throw IndexOutOfBoundsException("Array is $size bytes long, expected 16 bytes")
    }
    return getUuid(0, order)
}