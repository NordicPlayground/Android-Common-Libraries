package no.nordicsemi.android.navigation

import android.os.Parcelable
import java.util.*

sealed class DestinationArgument

data class StringDestinationArgument(val value: String) : DestinationArgument()

data class UUIDArgument(val value: UUID) : DestinationArgument()

data class ParcelableArgument(val value: Parcelable) : DestinationArgument()
