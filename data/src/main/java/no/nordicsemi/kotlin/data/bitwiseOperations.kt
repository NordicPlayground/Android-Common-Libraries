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

import kotlin.experimental.and
import kotlin.experimental.xor

/**
 * Returns whether ALL bits that are equal to 1 in the given [mask] are also set to 1
 * in the receiver.
 *
 * Example:
 * - 0b00001111 hasAllBitsSet 0b00000111 == true
 * - 0b00001111 hasAllBitsSet 0b00001000 == true
 * - 0b10101010 hasAllBitsSet 0b00000100 == false
 * - 0b10101000 hasAllBitsSet 0b10101010 == false
 * - 0b10101010 hasAllBitsSet 0b10000000 == true
 * @receiver Byte value.
 */
infix fun Byte.hasAllBitsSet(mask: Int): Boolean = this and mask.toByte() == mask.toByte()

/**
 * Returns whether ALL bits that are equal to 1 in the given [mask] are set to 0
 * in the receiver.
 *
 * Example:
 * - 0b00001111 hasAllBitsCleared 0b00000111 == false
 * - 0b00001111 hasAllBitsCleared 0b00001000 == false
 * - 0b10101010 hasAllBitsCleared 0b00000100 == true
 * - 0b10101000 hasAllBitsCleared 0b10101010 == false
 * - 0b10101010 hasAllBitsCleared 0b10000000 == false
 * @receiver Byte value.
 */
infix fun Byte.hasAllBitsCleared(mask: Int): Boolean = this and mask.toByte() == 0.toByte()

/**
 * Returns whether ALL bits that are equal to 1 in the given [mask] are also set to 1
 * in the receiver.
 *
 * Example:
 * - 0b00001111 hasAllBitsSet 0b00000111 == true
 * - 0b00001111 hasAllBitsSet 0b00001000 == true
 * - 0b10101010 hasAllBitsSet 0b00000100 == false
 * - 0b10101000 hasAllBitsSet 0b10101010 == false
 * - 0b10101010 hasAllBitsSet 0b10000000 == true
 * @receiver Byte value.
 */
infix fun UByte.hasAllBitsSet(mask: Int): Boolean = this.toByte().hasAllBitsSet(mask)

/**
 * Returns whether ALL bits that are equal to 1 in the given [mask] are set to 0
 * in the receiver.
 *
 * Example:
 * - 0b00001111 hasAllBitsCleared 0b00000111 == false
 * - 0b00001111 hasAllBitsCleared 0b00001000 == false
 * - 0b10101010 hasAllBitsCleared 0b00000100 == true
 * - 0b10101000 hasAllBitsCleared 0b10101010 == false
 * - 0b10101010 hasAllBitsCleared 0b10000000 == false
 * @receiver Byte value.
 */
infix fun UByte.hasAllBitsCleared(mask: Int): Boolean = this.toByte().hasAllBitsCleared(mask)

// -------------------------------------------------------------------------------------------------

/**
 * Returns whether the bit at the given [bit] is set to 1 in the receiver.
 * @receiver Byte value.
 */
infix fun Byte.hasBitSet(bit: Int): Boolean = this and (1 shl bit).toByte() != 0.toByte()

/**
 * Returns whether the bit at the given [bit] is set to 0 in the receiver.
 * @receiver Byte value.
 */
infix fun Byte.hasBitCleared(bit: Int): Boolean = this and (1 shl bit).toByte() == 0.toByte()

/**
 * Returns whether the bit at the given [bit] is set to 1 in the receiver.
 * @receiver UByte value.
 */
infix fun UByte.hasBitSet(bit: Int): Boolean = this.toByte().hasBitSet(bit)

/**
 * Returns whether the bit at the given [bit] is set to 0 in the receiver.
 * @receiver UByte value.
 */
infix fun UByte.hasBitCleared(bit: Int): Boolean = this.toByte().hasBitCleared(bit)

/**
 * Returns whether the bit at the given [bit] is set to 1 in the receiver.
 * @receiver Short value.
 */
infix fun Short.hasBitSet(bit: Int): Boolean = this and (1 shl bit).toShort() != 0.toShort()

/**
 * Returns whether the bit at the given [bit] is set to 0 in the receiver.
 * @receiver Short value.
 */
infix fun Short.hasBitCleared(bit: Int): Boolean = this and (1 shl bit).toShort() == 0.toShort()

/**
 * Returns whether the bit at the given [bit] is set to 1 in the receiver.
 * @receiver UShort value.
 */
infix fun UShort.hasBitSet(bit: Int): Boolean = this and (1 shl bit).toUShort() != 0.toUShort()

/**
 * Returns whether the bit at the given [bit] is set to 0 in the receiver.
 * @receiver UShort value.
 */
infix fun UShort.hasBitCleared(bit: Int): Boolean = this and (1 shl bit).toUShort() == 0.toUShort()

/**
 * Returns whether the bit at the given [bit] is set to 1 in the receiver.
 * @receiver Int value.
 */
infix fun Int.hasBitSet(bit: Int): Boolean = this and (1 shl bit) != 0

/**
 * Returns whether the bit at the given [bit] is set to 0 in the receiver.
 * @receiver Int value.
 */
infix fun Int.hasBitCleared(bit: Int): Boolean = this and (1 shl bit) == 0

/**
 * Returns whether the bit at the given [bit] is set to 1 in the receiver.
 * @receiver UInt value.
 */
infix fun UInt.hasBitSet(bit: Int): Boolean = this and (1u shl bit) != 0u

/**
 * Returns whether the bit at the given [bit] is set to 0 in the receiver.
 * @receiver UInt value.
 */
infix fun UInt.hasBitCleared(bit: Int): Boolean = this and (1u shl bit) == 0u

// -------------------------------------------------------------------------------------------------

/**
 * Applies the XOR operator on two byte arrays. Compared to already existent
 * xor functions, this one does not require the arrays to be of the same length.
 *
 * @param other The other byte array which is xor ed with this one.
 * @return XOR of the two byte arrays.
 */
infix fun ByteArray.xor(other: ByteArray): ByteArray {
    val result = ByteArray(this.count())
    for (i in this.indices) {
        result[i] = (this[i] xor other[i % other.count()])
    }
    return result
}

/**
 * Shifts this value left by the [bitCount] number of bits.
 *
 * Note that only the three lowest-order bits of the [bitCount] are used as the shift distance.
 * The shift distance actually used is therefore always in the range `0..7`.
 */
infix fun Byte.shl(bitCount: Int): Byte = (this.toInt() shl bitCount).toByte()

/**
 * Shifts this value right by the [bitCount] number of bits, filling the leftmost bits with
 * copies of the sign bit.
 *
 * Note that only the three lowest-order bits of the [bitCount] are used as the shift distance.
 * The shift distance actually used is therefore always in the range `0..7`.
 */
infix fun Byte.shr(bitCount: Int): Byte = (this.toInt() shr bitCount).toByte()

/**
 * Shifts this value right by the [bitCount] number of bits, filling the leftmost bits with zeros.
 *
 * Note that only the three lowest-order bits of the [bitCount] are used as the shift distance.
 * The shift distance actually used is therefore always in the range `0..7`.
 */
infix fun Byte.ushr(bitCount: Int): Byte = ((this.toInt() and 0xFF) ushr bitCount).toByte()

/**
 * Shifts this value left by the [bitCount] number of bits.
 *
 * Note that only the three lowest-order bits of the [bitCount] are used as the shift distance.
 * The shift distance actually used is therefore always in the range `0..7`.
 */
infix fun UByte.shl(bitCount: Int): UByte = (this.toUInt() shl bitCount).toUByte()

/**
 * Shifts this value right by the [bitCount] number of bits, filling the leftmost bits with zeros.
 *
 * Note that only the three lowest-order bits of the [bitCount] are used as the shift distance.
 * The shift distance actually used is therefore always in the range `0..7`.
 */
infix fun UByte.shr(bitCount: Int): UByte = (this.toUInt() shr bitCount).toUByte()

/**
 * Shifts this value left by the [bitCount] number of bits.
 *
 * Note that only the four lowest-order bits of the [bitCount] are used as the shift distance.
 * The shift distance actually used is therefore always in the range `0..15`.
 */
infix fun Short.shl(bitCount: Int): Short = (this.toInt() shl bitCount).toShort()

/**
 * Shifts this value right by the [bitCount] number of bits, filling the leftmost bits with
 * copies of the sign bit.
 *
 * Note that only the four lowest-order bits of the [bitCount] are used as the shift distance.
 * The shift distance actually used is therefore always in the range `0..15`.
 */
infix fun Short.shr(bitCount: Int): Short = (this.toInt() ushr bitCount).toShort()

/**
 * Shifts this value right by the [bitCount] number of bits, filling the leftmost bits with
 * copies of the sign bit.
 *
 * Note that only the four lowest-order bits of the [bitCount] are used as the shift distance.
 * The shift distance actually used is therefore always in the range `0..15`.
 */
infix fun Short.ushr(bitCount: Int): Short = ((this.toInt() and 0xFFFF) ushr bitCount).toShort()

/**
 * Shifts this value left by the [bitCount] number of bits.
 *
 * Note that only the four lowest-order bits of the [bitCount] are used as the shift distance.
 * The shift distance actually used is therefore always in the range `0..15`.
 */
infix fun UShort.shl(bitCount: Int): UShort = (this.toInt() shl bitCount).toUShort()

/**
 * Shifts this value right by the [bitCount] number of bits, filling the leftmost bits with
 * copies of the sign bit.
 *
 * Note that only the four lowest-order bits of the [bitCount] are used as the shift distance.
 * The shift distance actually used is therefore always in the range `0..15`.
 */
infix fun UShort.shr(bitCount: Int): UShort = (this.toInt() ushr bitCount).toUShort()
