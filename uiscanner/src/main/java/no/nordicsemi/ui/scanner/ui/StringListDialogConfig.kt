package no.nordicsemi.ui.scanner.ui

import androidx.annotation.DrawableRes
import androidx.compose.ui.text.AnnotatedString
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.scanner.repository.AllDevices
import no.nordicsemi.ui.scanner.scanner.repository.DeviceResource
import no.nordicsemi.ui.scanner.scanner.repository.LoadingResult
import no.nordicsemi.ui.scanner.scanner.repository.SuccessResult

internal data class StringListDialogConfig(
    val title: AnnotatedString? = null,
    @DrawableRes
    val leftIcon: Int? = null,
    val result: AllDevices,
    val filterItems: List<FilterItem> = emptyList(),
    val isLoading: Boolean = true,
    val onFilterItemCheckChanged: (Int) -> Unit,
    val onResult: (StringListDialogResult) -> Unit
) {

    fun isRunning(): Boolean {
        return result.discoveredDevices is LoadingResult || result.discoveredDevices is SuccessResult
    }
}

internal data class FilterItem(
    val text: String,
    val isChecked: Boolean = false,
    @DrawableRes
    val icon: Int? = null,
    val onClick: () -> Unit
)
