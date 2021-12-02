package no.nordicsemi.ui.scanner.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.buildAnnotatedString
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun String.toAnnotatedString() = buildAnnotatedString {
    append(this@toAnnotatedString)
}

fun <T> MutableStateFlow<T>.updateIfDifferent(newValue: T) {
    if (value != newValue) {
        value = newValue
    }
}

val <T> T.exhaustive
    get() = this
