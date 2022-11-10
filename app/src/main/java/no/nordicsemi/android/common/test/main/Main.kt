package no.nordicsemi.android.common.test.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.navigation.createSimpleDestination
import no.nordicsemi.android.common.navigation.defineDestination
import no.nordicsemi.android.common.test.main.page.*
import no.nordicsemi.android.common.test.scanner.ScannerDestination
import no.nordicsemi.android.common.test.simple.HelloDestinations
import no.nordicsemi.android.common.theme.view.PagerView
import no.nordicsemi.android.common.theme.view.PagerViewEntity

/** This is the destination identifier. */
val Main = createSimpleDestination("main")

/**
 * Here you define a View for the destination.
 */
private val MainDestination = defineDestination(Main) {
    MainScreen()
}

val MainDestinations = MainDestination + ScannerDestination + HelloDestinations

@Composable
private fun MainScreen() {
    val pages = PagerViewEntity(listOf(
        BasicsPage,
        FontsPage,
        WizardPage,
        ConnectionPage,
        WarningPage,
    ))
    PagerView(
        viewEntity = pages,
        contentPadding = PaddingValues(horizontal = 16.dp),
        itemSpacing = 16.dp,
    )
}