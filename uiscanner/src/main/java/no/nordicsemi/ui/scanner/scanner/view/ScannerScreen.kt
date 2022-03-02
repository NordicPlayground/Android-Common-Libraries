package no.nordicsemi.ui.scanner.scanner.view

import android.os.ParcelUuid
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
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
        AppBar(stringResource(id = R.string.scanner_screen)) { onEvent(NavigateUp) }

        val showSearchDialog = rememberSaveable { mutableStateOf(true) }

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            ScanEmptyView(requireLocation) {
                showSearchDialog.value = true
            }
        }

        if (showSearchDialog.value) {
            ShowSearchDialog(
                { showSearchDialog.value = false },
                config,
                onEvent
            )
        }
    }
}

@Composable
internal fun ShowSearchDialog(
    hideDialog: () -> Unit,
    config: DevicesScanFilter,
    onEvent: (PermissionsViewEvent) -> Unit,
) {
    val viewModel = hiltViewModel<ScannerViewModel>()
    val result = viewModel.devices.collectAsState().value

    val uuidIsCheckedFilter = config.filterUuidRequired
    val nearbyIsCheckedFilter = config.filterNearbyOnly
    val nameCheckedFilter = config.filterWithNames

    val uuidFilter = uuidIsCheckedFilter?.let {
        FilterItem(
            stringResource(id = R.string.filter_uuid),
            it,
            R.drawable.ic_filter_uuid
        ) {
            viewModel.filterByUuid(!uuidIsCheckedFilter)
        }
    }
    val nearbyFilter = FilterItem(
        stringResource(id = R.string.filter_nearby),
        nearbyIsCheckedFilter,
        R.drawable.ic_filter_nearby
    ) {
        viewModel.filterByDistance(!nearbyIsCheckedFilter)
    }
    val nameFilter = FilterItem(
        stringResource(id = R.string.filter_name),
        nameCheckedFilter,
        R.drawable.ic_face
    ) {
        viewModel.filterByName(!nameCheckedFilter)
    }
    val filters = listOfNotNull(uuidFilter, nearbyFilter, nameFilter)

    val config = StringListDialogConfig(
        title = stringResource(id = R.string.devices).toAnnotatedString(),
        result = result,
        filterItems = filters,
        leftIcon = R.drawable.ic_bluetooth,
        onFilterItemCheckChanged = { filters[it].onClick() }
    ) {
        when (it) {
            FlowCanceled -> hideDialog()
            is ItemSelectedResult -> {
                onEvent(DeviceSelected(it.value))
            }
        }
    }
    StringListDialog(config)
}
