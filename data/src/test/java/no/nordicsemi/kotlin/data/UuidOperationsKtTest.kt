package no.nordicsemi.kotlin.data


import java.nio.ByteOrder
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class UuidOperationsKtTest {

    @Test
    fun toByteArray_BigEndian() {
        val uuid = UUID.fromString("00112233-4455-6677-8899-AABBCCDDEEFF")
        val byteArray = uuid.toByteArray(ByteOrder.BIG_ENDIAN)
        val expected = byteArrayOf(
            0x00, 0x11, 0x22, 0x33,
            0x44, 0x55, 0x66, 0x77,
            0x88.toByte(), 0x99.toByte(), 0xAA.toByte(), 0xBB.toByte(),
            0xCC.toByte(), 0xDD.toByte(), 0xEE.toByte(), 0xFF.toByte(),
        )
        assertContentEquals(expected, byteArray)
    }

    @Test
    fun toByteArray_LittleEndian() {
        val uuid = UUID.fromString("00001530-1212-EFDE-1523-785FEABCD123") // Legacy DFU Service UUID
        val byteArray = uuid.toByteArray(ByteOrder.LITTLE_ENDIAN)
        val expected = byteArrayOf(
            0x23, 0xD1.toByte(), 0xBC.toByte(), 0xEA.toByte(),
            0x5F, 0x78, 0x23, 0x15,
            0xDE.toByte(), 0xEF.toByte(), 0x12, 0x12,
            0x30, 0x15, 0x00, 0x00,
        )
        assertContentEquals(expected, byteArray)
    }

    @Test
    fun getUuid_BigEndian() {
        val array = byteArrayOf(
            0x00, 0x11, 0x22, 0x33, // offset
            0x00, 0x11, 0x22, 0x33,
            0x44, 0x55, 0x66, 0x77,
            0x88.toByte(), 0x99.toByte(), 0xAA.toByte(), 0xBB.toByte(),
            0xCC.toByte(), 0xDD.toByte(), 0xEE.toByte(), 0xFF.toByte(),
        )
        val uuid = array.getUuid(4, ByteOrder.BIG_ENDIAN)
        val expected = UUID.fromString("00112233-4455-6677-8899-AABBCCDDEEFF")
        assertEquals(expected, uuid)
    }

    @Test
    fun getUuid_LittleEndian() {
        val array = byteArrayOf(
            0x00, 0x11, 0x22, 0x33, // offset
            0x23, 0xD1.toByte(), 0xBC.toByte(), 0xEA.toByte(),
            0x5F, 0x78, 0x23, 0x15,
            0xDE.toByte(), 0xEF.toByte(), 0x12, 0x12,
            0x30, 0x15, 0x00, 0x00,
        )
        val uuid = array.getUuid(4, ByteOrder.LITTLE_ENDIAN)
        val expected = UUID.fromString("00001530-1212-EFDE-1523-785FEABCD123")
        assertEquals(expected, uuid)
    }

    @Test
    fun toUuid_BigEndian() {
        val array = byteArrayOf(
            0x00, 0x11, 0x22, 0x33,
            0x44, 0x55, 0x66, 0x77,
            0x88.toByte(), 0x99.toByte(), 0xAA.toByte(), 0xBB.toByte(),
            0xCC.toByte(), 0xDD.toByte(), 0xEE.toByte(), 0xFF.toByte(),
        )
        val uuid = array.toUuid(ByteOrder.BIG_ENDIAN)
        val expected = UUID.fromString("00112233-4455-6677-8899-AABBCCDDEEFF")
        assertEquals(expected, uuid)
    }

    @Test
    fun toUuid_LittleEndian() {
        val array = byteArrayOf(
            0x23, 0xD1.toByte(), 0xBC.toByte(), 0xEA.toByte(),
            0x5F, 0x78, 0x23, 0x15,
            0xDE.toByte(), 0xEF.toByte(), 0x12, 0x12,
            0x30, 0x15, 0x00, 0x00,
        )
        val uuid = array.toUuid(ByteOrder.LITTLE_ENDIAN)
        val expected = UUID.fromString("00001530-1212-EFDE-1523-785FEABCD123")
        assertEquals(expected, uuid)
    }
}