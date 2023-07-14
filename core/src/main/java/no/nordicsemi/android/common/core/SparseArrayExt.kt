package no.nordicsemi.android.common.core

import android.util.SparseArray

fun <T, R> SparseArray<T>.map(
    modifier: (T) -> R,
): SparseArray<R> {
    val newArray = SparseArray<R>(this.size())
    for (i in 0..this.size()) {
        newArray[i] = modifier(this[i])
    }
    return newArray
}
