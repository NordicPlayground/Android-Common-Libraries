package no.nordicsemi.ui.scanner.scanner.view

import android.os.ParcelUuid
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.navigation.doNothing
import no.nordicsemi.ui.scanner.R
import no.nordicsemi.ui.scanner.permissions.DeviceSelected
import no.nordicsemi.ui.scanner.permissions.NavigateUp
import no.nordicsemi.ui.scanner.permissions.PermissionsViewEvent
import no.nordicsemi.ui.scanner.scanner.repository.DevicesScanFilter
import no.nordicsemi.ui.scanner.scanner.viewmodel.ScannerViewModel
import no.nordicsemi.ui.scanner.ui.*

@Composable
internal fun ScannerScreen(
    uuid: ParcelUuid?,
    onEvent: (PermissionsViewEvent) -> Unit
) {
    val viewModel = hiltViewModel<ScannerViewModel>().apply {
        setFilterUuid(uuid)
    }
    val requireLocation = viewModel.dataProvider.locationState.collectAsState().value
    val config = viewModel.config.collectAsState().value

    Column {
        val dialogConfig = createConfig(config, onEvent)

        AppBar(stringResource(id = R.string.scanner_screen)) { onEvent(NavigateUp) }

        DevicesListView(requireLocation, dialogConfig)
    }
}

@Composable
private fun createConfig(
    config: DevicesScanFilter,
    onEvent: (PermissionsViewEvent) -> Unit
): StringListDialogConfig {

    val viewModel = hiltViewModel<ScannerViewModel>()
    val result = viewModel.devices.collectAsState().value

    val uuidIsCheckedFilter = config.filterUuidRequired
    val nearbyIsCheckedFilter = config.filterNearbyOnly
    val nameCheckedFilter = config.filterWithNames

    val uuidFilter = uuidIsCheckedFilter?.let {
        FilterItem(
            stringResource(id = R.string.filter_uuid),
            it
        ) {
            viewModel.filterByUuid(!uuidIsCheckedFilter)
        }
    }
    val nearbyFilter = FilterItem(
        stringResource(id = R.string.filter_nearby),
        nearbyIsCheckedFilter
    ) {
        viewModel.filterByDistance(!nearbyIsCheckedFilter)
    }
    val nameFilter = FilterItem(
        stringResource(id = R.string.filter_name),
        nameCheckedFilter
    ) {
        viewModel.filterByName(!nameCheckedFilter)
    }
    val filters = listOfNotNull(uuidFilter, nearbyFilter, nameFilter)

    return StringListDialogConfig(
        title = stringResource(id = R.string.devices).toAnnotatedString(),
        result = result,
        filterItems = filters,
        leftIcon = R.drawable.ic_bluetooth,
        onFilterItemCheckChanged = { filters[it].onClick() }
    ) {
        when (it) {
            FlowCanceled -> doNothing()
            is ItemSelectedResult -> onEvent(DeviceSelected(it.value))
        }
    }
}
