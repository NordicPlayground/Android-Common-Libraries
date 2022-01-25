package no.nordicsemi.android.navigation

import android.os.Parcelable
import java.util.*

data class DestinationArgument(
    val destinationId: DestinationId,
    val argument: Argument
)

sealed class Argument

data class StringDestinationArgument(
    val value: String,
) : Argument()

data class UUIDArgument(
    val value: UUID,
) : Argument()

data class ParcelableArgument(
    val value: Parcelable,
) : Argument()

data class AnyArgument(
    val value: Any,
) : Argument()
