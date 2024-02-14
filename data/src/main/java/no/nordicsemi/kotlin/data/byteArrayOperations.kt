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

/**
 * Converts an Int to a byte array using the given endianness.
 * @param order The byte order, default is [ByteOrder.BIG_ENDIAN].
 */
fun Int.toByteArray(order: ByteOrder = ByteOrder.BIG_ENDIAN) = when (order) {
    ByteOrder.BIG_ENDIAN -> ByteArray(4) { (this ushr (24 - it * 8)).toByte() }
    else ->                 ByteArray(4) { (this ushr (it * 8)).toByte() }
}

/**
 * Converts an UInt to a byte array using the given endianness.
 * @param order The byte order, default is [ByteOrder.BIG_ENDIAN].
 */
fun UInt.toByteArray(order: ByteOrder = ByteOrder.BIG_ENDIAN) = when (order) {
    ByteOrder.BIG_ENDIAN -> ByteArray(4) { (this shr (24 - it * 8)).toByte() }
    else ->                 ByteArray(4) { (this shr (it * 8)).toByte() }
}

/**
 * Converts a Short to a byte array using the given endianness.
 * @param order The byte order, default is [ByteOrder.BIG_ENDIAN].
 */
fun Short.toByteArray(order: ByteOrder = ByteOrder.BIG_ENDIAN) = when (order) {
    ByteOrder.BIG_ENDIAN -> ByteArray(2) { (this ushr (8 - it * 8)).toByte() }
    else ->                 ByteArray(2) { (this ushr (it * 8)).toByte() }
}

/**
 * Converts a UShort to a byte array using the given endianness.
 * @param order The byte order, default is [ByteOrder.BIG_ENDIAN].
 */
fun UShort.toByteArray(order: ByteOrder = ByteOrder.BIG_ENDIAN) = when (order) {
    ByteOrder.BIG_ENDIAN -> ByteArray(2) { (this shr (8 - it * 8)).toByte() }
    else ->                 ByteArray(2) { (this shr (it * 8)).toByte() }
}

/**
 * Converts a Byte to a byte array.
 */
fun Byte.toByteArray() = ByteArray(1) { this }

/**
 * Converts a UByte to a byte array.
 */
fun UByte.toByteArray() = ByteArray(1) { this.toByte() }

//--------------------------------------------------------------------------------------------------

/**
 * Returns an Int from a byte array with a given offset.
 *
 * @param offset The index to start from.
 * @param order The byte order, default is [ByteOrder.BIG_ENDIAN].
 * @return Int.
 * @throws IllegalArgumentException If the length of byte array is not >= offset + 4.
 */
fun ByteArray.getInt(offset: Int, order: ByteOrder = ByteOrder.BIG_ENDIAN): Int {
    require(offset >= 0 && size >= offset + 4) {
        throw IndexOutOfBoundsException("Cannot return an Int from an array of size $size from offset $offset")
    }
    return ByteBuffer.wrap(this, offset, 4).order(order).int
}

/**
 * Returns an Int from a byte array with a given offset.
 *
 * @param offset The index to start from.
 * @param order The byte order, default is [ByteOrder.BIG_ENDIAN].
 * @return UInt.
 * @throws IllegalArgumentException If the length of byte array is not >= offset + 4.
 */
fun ByteArray.getUInt(offset: Int, order: ByteOrder = ByteOrder.BIG_ENDIAN): UInt {
    require(offset >= 0 && size >= offset + 4) {
        throw IndexOutOfBoundsException("Cannot return an UInt from an array of size $size from offset $offset")
    }
    return ByteBuffer.wrap(this, offset, 4).order(order).int.toUInt()
}

/**
 * Returns an Int from a byte array with a given offset.
 *
 * @param offset The index to start from.
 * @param order The byte order, default is [ByteOrder.BIG_ENDIAN].
 * @return Short.
 * @throws IndexOutOfBoundsException If the length of the byte array is not >= offset + 2.
 */
fun ByteArray.getShort(offset: Int, order: ByteOrder = ByteOrder.BIG_ENDIAN): Short {
    require(offset >= 0 && size >= offset + 2) {
        throw IndexOutOfBoundsException("Cannot return a Short from an array of size $size from offset $offset")
    }
    return ByteBuffer.wrap(this, offset, 2).order(order).short
}

/**
 * Returns an Int from a byte array with a given offset.
 *
 * @param offset The index to start from.
 * @param order The byte order, default is [ByteOrder.BIG_ENDIAN].
 * @return UShort.
 * @throws IndexOutOfBoundsException If the length of the byte array is not >= offset + 2.
 */
fun ByteArray.getUShort(offset: Int, order: ByteOrder = ByteOrder.BIG_ENDIAN): UShort {
    require(offset >= 0 && size >= offset + 2) {
        throw IndexOutOfBoundsException("Cannot return a UShort from an array of size $size from offset $offset")
    }
    return ByteBuffer.wrap(this, offset, 2).order(order).short.toUShort()
}