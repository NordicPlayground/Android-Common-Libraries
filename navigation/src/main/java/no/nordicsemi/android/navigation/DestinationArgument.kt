package no.nordicsemi.android.navigation

import android.os.Parcelable
import java.util.*

sealed class DestinationArgument {
    abstract val destinationId: DestinationId
}

data class StringDestinationArgument(
    override val destinationId: DestinationId,
    val value: String,
) : DestinationArgument()

data class UUIDArgument(
    override val destinationId: DestinationId,
    val value: UUID,
) : DestinationArgument()

data class ParcelableArgument(
    override val destinationId: DestinationId,
    val value: Parcelable,
) : DestinationArgument()
