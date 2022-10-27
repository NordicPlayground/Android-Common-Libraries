package no.nordicsemi.android.common.navigation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import no.nordicsemi.android.common.navigation.DestinationId
import no.nordicsemi.android.common.navigation.Navigator
import no.nordicsemi.android.common.navigation.get
import no.nordicsemi.android.common.navigation.getOrNull
import javax.inject.Inject

@Suppress("unused")
@HiltViewModel
open class SimpleNavigationViewModel @Inject constructor(
    navigator: Navigator,
    private val savedStateHandle: SavedStateHandle,
): ViewModel(), Navigator by navigator {

    /**
     * Returns the parameter of the current destination, or null, if hasn't been set.
     */
    fun <A> nullableParameterOf(destinationId: DestinationId<A?, *>): A? =
        savedStateHandle.getOrNull(destinationId)

    /**
     * Returns the parameter of the current destination, or null, if hasn't been set.
     */
    fun <A> parameterOf(destinationId: DestinationId<A & Any, *>): A =
        savedStateHandle.get(destinationId)

    /**
     * Returns the parameter of the current destination, or null, if hasn't been set.
     */
    fun parameterOf(destinationId: DestinationId<Unit, *>): Nothing =
        savedStateHandle.get(destinationId)

    /**
     * Returns the parameter of the current destination, or null, if hasn't been set.
     */
    protected fun <A> DestinationId<A?, *>.getParameterOrNull(): A? =
        nullableParameterOf(this)

    /**
     * Returns the parameter of the current destination.
     */
    protected fun <A> DestinationId<A & Any, *>.getParameter(): A =
        parameterOf(this)

}