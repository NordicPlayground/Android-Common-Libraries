package no.nordicsemi.kotlin.data

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class BitwiseOperationsKtTest {

    @Test
    fun hasAllBitsSet() {
        val value = 0b1010_1001.toByte()
        assertTrue(value.hasAllBitsSet(0b1000_0001))
        assertTrue(value.hasAllBitsSet(0b1010_1001))
        assertTrue(value.hasAllBitsSet(0b0000_0001))
        assertTrue(value.hasAllBitsSet(0b0010_1000))
        assertTrue(value.hasAllBitsSet(0b0000_0000))
        assertFalse(value.hasAllBitsSet(0b1100_0001))
        assertFalse(value.hasAllBitsSet(0b1000_0010))
        assertFalse(value.hasAllBitsSet(0b1010_0101))
        assertFalse(value.hasAllBitsSet(0b1010_1111))
        assertFalse(value.hasAllBitsSet(0b0000_1100))
    }

    @Test
    fun hasAllBitsCleared() {
        val value = 0b1010_1001.toByte()
        assertFalse(value.hasAllBitsCleared(0b1111_1111))
        assertFalse(value.hasAllBitsCleared(0b1000_0001))
        assertFalse(value.hasAllBitsCleared(0b1010_1001))
        assertFalse(value.hasAllBitsCleared(0b0000_0001))
        assertFalse(value.hasAllBitsCleared(0b0010_1000))
        assertTrue(value.hasAllBitsCleared(0b0000_0000))
        assertTrue(value.hasAllBitsCleared(0b0100_0010))
        assertTrue(value.hasAllBitsCleared(0b0101_0110))
        assertTrue(value.hasAllBitsCleared(0b0000_0100))
        assertTrue(value.hasAllBitsCleared(0b0001_0000))
    }

    @Test
    fun bitTests() {
        val value = 0b1010_1001.toByte()
        assertTrue(value.hasBitSet(0))
        assertFalse(value.hasBitCleared(0))
        assertTrue(value.hasBitCleared(1))
        assertFalse(value.hasBitSet(1))
        assertTrue(value.hasBitCleared(2))
        assertFalse(value.hasBitSet(2))
        assertTrue(value.hasBitSet(3))
        assertFalse(value.hasBitCleared(3))
        assertTrue(value.hasBitCleared(4))
        assertFalse(value.hasBitSet(4))
        assertTrue(value.hasBitSet(5))
        assertFalse(value.hasBitCleared(5))
        assertTrue(value.hasBitCleared(6))
        assertFalse(value.hasBitSet(6))
        assertTrue(value.hasBitSet(7))
        assertFalse(value.hasBitCleared(7))
    }

    @Test
    fun bitTestsInt() {
        val value = 0x2345_6789
        assertTrue(value.hasBitSet(0))
        assertFalse(value.hasBitCleared(0))
        assertTrue(value.hasBitCleared(31))
        assertFalse(value.hasBitSet(31))
    }

    @Test
    fun xor() {
        val left = byteArrayOf(0xF1.toByte(), 0x02, 0x03, 0x04)
        val right = byteArrayOf(0x25, 0x06, 0x07, 0x08)
        val xor = left xor right
        assertContentEquals(byteArrayOf(0xD4.toByte(), 0x04, 0x04, 0x0C), xor)
    }

    @Test
    fun byteShl() {
        val value = 0b1010_1001.toByte()
        assertEquals(0b0101_0010.toByte(), value shl 1)
        assertEquals(0b1001_0000.toByte(), value shl 4)
        assertEquals(0b1000_0000.toByte(), value shl 7)
        assertEquals(0b0000_0000.toByte(), value shl 8)
    }

    @Test
    fun byteShr() {
        val value = 0b1010_1001.toByte()
        assertEquals(0b1101_0100.toByte(), value shr 1)
        assertEquals(0b1111_1010.toByte(), value shr 4)
        assertEquals(0b1111_1111.toByte(), value shr 7)
        assertEquals(0b1111_1111.toByte(), value shr 8)
    }

    @Test
    fun byteUshr() {
        val value = 0b1010_1001.toByte()
        assertEquals(0b0101_0100.toByte(), value ushr 1)
        assertEquals(0b0000_1010.toByte(), value ushr 4)
        assertEquals(0b0000_0001.toByte(), value ushr 7)
        assertEquals(0b0000_0000.toByte(), value ushr 8)
    }

    @Test
    fun uByteShl() {
        val value = 0b1010_1001.toUByte()
        assertEquals(0b0101_0010.toUByte(), value shl 1)
        assertEquals(0b1001_0000.toUByte(), value shl 4)
        assertEquals(0b1000_0000.toUByte(), value shl 7)
        assertEquals(0b0000_0000.toUByte(), value shl 8)
    }

    @Test
    fun uByteShr() {
        val value = 0b1010_1001.toUByte()
        assertEquals(0b0101_0100.toUByte(), value shr 1)
        assertEquals(0b0000_1010.toUByte(), value shr 4)
        assertEquals(0b0000_0001.toUByte(), value shr 7)
        assertEquals(0b0000_0000.toUByte(), value shr 8)
    }

    @Test
    fun shortShl() {
        val value = 0b1010_1001_1010_1001.toShort()
        assertEquals(0b0101_0011_0101_0010.toShort(), value shl 1)
        assertEquals(0b1001_1010_1001_0000.toShort(), value shl 4)
        assertEquals(0b1000_0000_0000_0000.toShort(), value shl 15)
        assertEquals(0b0000_0000_0000_0000.toShort(), value shl 16)
    }

    @Test
    fun shortShr() {
        val value = 0b1010_1001_1010_1001.toShort()
        assertEquals(0b1101_0100_1101_0100.toShort(), value shr 1)
        assertEquals(0b1111_1010_1001_1010.toShort(), value shr 4)
        assertEquals(0b1111_1111_1111_0101.toShort(), value shr 11)
        assertEquals(0b1111_1111_1111_1111.toShort(), value shr 16)
    }

    @Test
    fun shortUshr() {
        val value = 0b1010_1001_1010_1001.toShort()
        assertEquals(0b0101_0100_1101_0100.toShort(), value ushr 1)
        assertEquals(0b0000_1010_1001_1010.toShort(), value ushr 4)
        assertEquals(0b0000_0000_0001_0101.toShort(), value ushr 11)
        assertEquals(0b0000_0000_0000_0000.toShort(), value ushr 16)
    }

    @Test
    fun uShortShl() {
        val value = 0b1010_1001_1010_1001.toUShort()
        assertEquals(0b0101_0011_0101_0010.toUShort(), value shl 1)
        assertEquals(0b1001_1010_1001_0000.toUShort(), value shl 4)
        assertEquals(0b1000_0000_0000_0000.toUShort(), value shl 15)
        assertEquals(0b0000_0000_0000_0000.toUShort(), value shl 16)
    }

    @Test
    fun uShortShr() {
        val value = 0b1010_1001_1010_1001.toUShort()
        assertEquals(0b0101_0100_1101_0100.toUShort(), value shr 1)
        assertEquals(0b0000_1010_1001_1010.toUShort(), value shr 4)
        assertEquals(0b0000_0000_0001_0101.toUShort(), value shr 11)
        assertEquals(0b0000_0000_0000_0000.toUShort(), value shr 16)
    }
}