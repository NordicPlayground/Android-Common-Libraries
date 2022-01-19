package no.nordicsemi.android.navigation

sealed class DestinationResult

object CancelDestinationResult : DestinationResult()

data class SuccessDestinationResult(val argument: DestinationArgument) : DestinationResult()
