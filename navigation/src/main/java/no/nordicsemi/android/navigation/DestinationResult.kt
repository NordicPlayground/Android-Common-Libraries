package no.nordicsemi.android.navigation

sealed class DestinationResult {
    abstract val destinationId: DestinationId
}

class CancelDestinationResult(
    override val destinationId: DestinationId,
) : DestinationResult()

data class SuccessDestinationResult(
    override val destinationId: DestinationId,
    val argument: DestinationArgument,
) : DestinationResult()
