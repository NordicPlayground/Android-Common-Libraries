package no.nordicsemi.android.common.scanner.data

import no.nordicsemi.android.common.scanner.viewmodel.FilterUiState

internal sealed class FilterConfig {
    data object Disabled : FilterConfig()

    data class Enabled(val filter: FilterUiState) : FilterConfig()
}
