package no.nordicsemi.ui.scanner.ui

import androidx.annotation.DrawableRes
import androidx.compose.ui.text.AnnotatedString
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import no.nordicsemi.ui.scanner.scanner.repository.AllDevices
import no.nordicsemi.ui.scanner.scanner.repository.DeviceResource

internal data class StringListDialogConfig(
    val title: AnnotatedString? = null,
    @DrawableRes
    val leftIcon: Int? = null,
    val result: AllDevices,
    val filterItems: List<FilterItem> = emptyList(),
    val isLoading: Boolean = true,
    val onFilterItemCheckChanged: (Int) -> Unit,
    val onResult: (StringListDialogResult) -> Unit
)

internal data class FilterItem(
    val text: String,
    val isChecked: Boolean = false,
    @DrawableRes
    val icon: Int? = null
)
