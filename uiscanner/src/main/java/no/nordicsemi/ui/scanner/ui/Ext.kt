package no.nordicsemi.ui.scanner.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.buildAnnotatedString
import kotlinx.coroutines.flow.MutableStateFlow
import no.nordicsemi.android.navigation.ParcelableArgument
import no.nordicsemi.android.navigation.SuccessDestinationResult
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice

@Composable
fun String.toAnnotatedString() = buildAnnotatedString {
    append(this@toAnnotatedString)
}

fun <T> MutableStateFlow<T>.updateIfDifferent(newValue: T) {
    if (value != newValue) {
        value = newValue
    }
}

val <T> T.exhaustive: Any
    get() = this as Any

fun SuccessDestinationResult.getDevice(): DiscoveredBluetoothDevice {
    return (argument as ParcelableArgument).value as DiscoveredBluetoothDevice
}
