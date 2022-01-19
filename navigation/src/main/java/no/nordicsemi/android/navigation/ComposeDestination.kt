package no.nordicsemi.android.navigation

import androidx.compose.runtime.Composable

class ComposeDestinations(val values: List<ComposeDestination>) {

    operator fun plus(other: ComposeDestinations): ComposeDestinations {
        return ComposeDestinations(this.values + other.values)
    }
}

data class ComposeDestination(
    val id: DestinationId,
    val draw: @Composable () -> Unit
) {

    constructor(id: String, draw: @Composable () -> Unit) : this(DestinationId(id), draw)
}
