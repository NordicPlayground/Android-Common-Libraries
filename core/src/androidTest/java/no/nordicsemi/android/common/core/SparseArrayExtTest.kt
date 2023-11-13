package no.nordicsemi.android.common.core

import android.util.SparseArray
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class SparseArrayExtTest {

    @Test
    fun whenMapSparseArrayShouldReturnArrayWithMappedValues() {
        val array = SparseArray<String>()

        array.put(0, "aaa")
        array.put(2, "bbb")
        array.put(10, "ccc")

        val newArray = array.map { "ddd" }

        val expectedArray = SparseArray<String>().apply {
            put(0, "ddd")
            put(2, "ddd")
            put(10, "ddd")
        }

        assertEquals(true, newArray.contentEquals(expectedArray))
    }
}
