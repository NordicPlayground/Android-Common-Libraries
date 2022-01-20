package no.nordicsemi.ui.scanner.bonding.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.ui.scanner.bonding.repository.BondingState
import no.nordicsemi.ui.scanner.bonding.viewmodel.BondingViewModel
import no.nordicsemi.ui.scanner.ui.exhaustive

@Composable
fun BondingScreen(finishAction: () -> Unit) {
    val viewModel = hiltViewModel<BondingViewModel>()
    val state = viewModel.state.collectAsState().value

    LaunchedEffect("start") {
        viewModel.bondDevice()
    }

    when (state) {
        BondingState.BONDING -> BondingInProgressView()
        BondingState.BONDED -> finishAction()
        BondingState.NONE -> BondingErrorView()
    }.exhaustive
}
