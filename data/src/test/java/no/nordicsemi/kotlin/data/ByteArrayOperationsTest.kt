package no.nordicsemi.kotlin.data

import java.nio.ByteOrder
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class ByteArrayOperationsTest {

    @Test
    fun intToByteArray() {
        val value: Int = -2023406815
        val bigEndian = value.toByteArray(ByteOrder.BIG_ENDIAN)
        val littleEndian = value.toByteArray(ByteOrder.LITTLE_ENDIAN)
        assertContentEquals(byteArrayOf(0x87.toByte(), 0x65, 0x43, 0x21), bigEndian)
        assertContentEquals(byteArrayOf(0x21, 0x43, 0x65, 0x87.toByte()), littleEndian)
    }

    @Test
    fun uIntToByteArray() {
        val value = 2271560481u
        val bigEndian = value.toByteArray(ByteOrder.BIG_ENDIAN)
        val littleEndian = value.toByteArray(ByteOrder.LITTLE_ENDIAN)
        assertContentEquals(byteArrayOf(0x87.toByte(), 0x65, 0x43, 0x21), bigEndian)
        assertContentEquals(byteArrayOf(0x21, 0x43, 0x65, 0x87.toByte()), littleEndian)
    }

    @Test
    fun shortToByteArray() {
        val value: Short = -30875
        val bigEndian = value.toByteArray(ByteOrder.BIG_ENDIAN)
        val littleEndian = value.toByteArray(ByteOrder.LITTLE_ENDIAN)
        assertContentEquals(byteArrayOf(0x87.toByte(), 0x65), bigEndian)
        assertContentEquals(byteArrayOf(0x65, 0x87.toByte()), littleEndian)
    }

    @Test
    fun uShortToByteArray() {
        val value: UShort = 34661u
        val bigEndian = value.toByteArray(ByteOrder.BIG_ENDIAN)
        val littleEndian = value.toByteArray(ByteOrder.LITTLE_ENDIAN)
        assertContentEquals(byteArrayOf(0x87.toByte(), 0x65), bigEndian)
        assertContentEquals(byteArrayOf(0x65, 0x87.toByte()), littleEndian)
    }

    @Test
    fun byteToByteArray() {
        val value: Byte = 0x87.toByte()
        val array = value.toByteArray()
        assertContentEquals(byteArrayOf(0x87.toByte()), array)
    }

    @Test
    fun uByteToByteArray() {
        val value: UByte = 0x87u
        val array = value.toByteArray()
        assertContentEquals(byteArrayOf(0x87.toByte()), array)
    }

    @Test
    fun getInt() {
        val array = byteArrayOf(0x87.toByte(), 0x65, 0x43, 0x21, 0x21, 0x43, 0x65, 0x87.toByte())
        val int1 = array.getInt(0, ByteOrder.BIG_ENDIAN)
        val int2 = array.getInt(4, ByteOrder.LITTLE_ENDIAN)
        assertEquals(int1, -2023406815)
        assertEquals(int2, -2023406815)
        assertFails {
            array.getInt(7, ByteOrder.LITTLE_ENDIAN)
        }
    }

    @Test
    fun getUInt() {
        val array = byteArrayOf(0x87.toByte(), 0x65, 0x43, 0x21, 0x21, 0x43, 0x65, 0x87.toByte())
        val int1 = array.getUInt(0, ByteOrder.BIG_ENDIAN)
        val int2 = array.getUInt(4, ByteOrder.LITTLE_ENDIAN)
        assertEquals(int1, 2271560481u)
        assertEquals(int2, 2271560481u)
        assertFails {
            array.getUInt(7, ByteOrder.BIG_ENDIAN)
        }
    }

    @Test
    fun getShort() {
        val array = byteArrayOf(0x87.toByte(), 0x65, 0x65, 0x87.toByte())
        val short1 = array.getShort(0, ByteOrder.BIG_ENDIAN)
        val short2 = array.getShort(2, ByteOrder.LITTLE_ENDIAN)
        assertEquals(short1, (-30875).toShort())
        assertEquals(short2, (-30875).toShort())
        assertFails {
            array.getShort(3, ByteOrder.BIG_ENDIAN)
        }
    }

    @Test
    fun getUShort() {
        val array = byteArrayOf(0x87.toByte(), 0x65, 0x65, 0x87.toByte())
        val short1 = array.getUShort(0, ByteOrder.BIG_ENDIAN)
        val short2 = array.getUShort(2, ByteOrder.LITTLE_ENDIAN)
        assertEquals(short1, 34661u.toUShort())
        assertEquals(short2, 34661u.toUShort())
        assertFails {
            array.getUShort(3, ByteOrder.LITTLE_ENDIAN)
        }
    }
}