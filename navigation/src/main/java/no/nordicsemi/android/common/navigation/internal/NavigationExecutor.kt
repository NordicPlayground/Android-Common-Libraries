package no.nordicsemi.android.common.navigation.internal

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import no.nordicsemi.android.common.navigation.DestinationId

/**
 * Navigation target. This class wraps the destination and the parameter.
 *
 * @property destination The destination id.
 * @property args Optional
 */
internal data class NavigationTarget<A>(
    val destination: DestinationId<A, *>,
    val args: @RawValue A
) {
    fun toBundle() = bundleOf(destination.name to args)
}

internal sealed class NavigationResultState
@Parcelize
internal object Initial : NavigationResultState(), Parcelable
@Parcelize
internal object Cancelled : NavigationResultState(), Parcelable
@Parcelize
internal data class Success<R>(val value: @RawValue R) : NavigationResultState(), Parcelable

/**
 * A navigation executor that can be used to navigate to next destination, or back.
 */
internal interface NavigationExecutor {
    /**
     * Navigates to the given destination passing an optional argument.
     *
     * @param target The target target with an optional parameter.
     */
    fun <A> navigate(target: NavigationTarget<A>)

    /**
     * Navigate up to the previous destination passing the given result.
     *
     * @param result The result, which will be passed to the previous destination.
     * The returned type will be saved in [SavedStateHandle], therefore it must be
     * savable to a [Bundle].
     */
    fun navigateUpWithResult(result: NavigationResultState)
}