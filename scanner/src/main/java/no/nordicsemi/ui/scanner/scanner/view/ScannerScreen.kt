package no.nordicsemi.ui.scanner.scanner.view

import android.os.ParcelUuid
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import no.nordicsemi.ui.scanner.R
import no.nordicsemi.ui.scanner.navigation.view.FindDeviceCloseResult
import no.nordicsemi.ui.scanner.navigation.view.FindDeviceFlowStatus
import no.nordicsemi.ui.scanner.scanner.repository.DeviceResource
import no.nordicsemi.ui.scanner.scanner.viewmodel.ScannerViewModel
import no.nordicsemi.ui.scanner.ui.AppBar
import no.nordicsemi.ui.scanner.ui.FilterItem
import no.nordicsemi.ui.scanner.ui.FlowCanceled
import no.nordicsemi.ui.scanner.ui.ItemSelectedResult
import no.nordicsemi.ui.scanner.ui.StringListDialog
import no.nordicsemi.ui.scanner.ui.StringListDialogConfig
import no.nordicsemi.ui.scanner.ui.toAnnotatedString
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun ScannerScreen(
    uuid: ParcelUuid,
    refreshNavigation: () -> Unit,
    onResult: (FindDeviceFlowStatus) -> Unit
) {

    Column {
        AppBar(stringResource(id = R.string.scanner_screen)) {
            onResult(FindDeviceCloseResult)
        }

        val showSearchDialog = remember { mutableStateOf(true) }

        ScanEmptyView {
            showSearchDialog.value = true
        }

        if (showSearchDialog.value) {
            ShowSearchDialog(uuid, refreshNavigation) {
                showSearchDialog.value = false
            }
        }
    }
}

@Composable
internal fun ShowSearchDialog(uuid: ParcelUuid, refreshNavigation: () -> Unit, hideDialog: () -> Unit) {
    val viewModel = getViewModel<ScannerViewModel> { parametersOf(uuid) }
    val result = viewModel.devices.collectAsState(DeviceResource.createLoading()).value

    val uuidIsCheckedFilter = rememberSaveable { mutableStateOf(false) }
    val nearbyIsCheckedFilter = rememberSaveable { mutableStateOf(false) }

    val uuidFilter = FilterItem(
        stringResource(id = R.string.filter_uuid),
        uuidIsCheckedFilter.value,
        R.drawable.ic_filter_uuid
    )
    val nearbyFilter = FilterItem(
        stringResource(id = R.string.filter_nearby),
        nearbyIsCheckedFilter.value,
        R.drawable.ic_filter_nearby
    )
    val filters = listOf(uuidFilter, nearbyFilter)

    val config = StringListDialogConfig(
        title = stringResource(id = R.string.devices).toAnnotatedString(),
        result = result,
        filterItems = filters,
        leftIcon = R.drawable.ic_bluetooth,
        onFilterItemCheckChanged = {
            if (it == 0) {
                uuidIsCheckedFilter.value = !uuidIsCheckedFilter.value
                viewModel.filterByUuid(uuidIsCheckedFilter.value)
            } else {
                nearbyIsCheckedFilter.value = !nearbyIsCheckedFilter.value
                viewModel.filterByDistance(nearbyIsCheckedFilter.value)
            }
        }
    ) {
        when (it) {
            FlowCanceled -> hideDialog()
            is ItemSelectedResult -> {
                viewModel.onDeviceSelected(it.value)
                refreshNavigation()
            }
        }
    }
    StringListDialog(config)
}
