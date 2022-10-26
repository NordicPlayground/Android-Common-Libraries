package no.nordicsemi.android.common.navigation

/**
 * The navigation result.
 */
sealed class NavigationResult<R> {
    /** Navigation was cancelled. */
    class Cancelled<R> : NavigationResult<R>()
    /** The navigation has returned a result. */
    data class Success<R>(val value: R) : NavigationResult<R>()
}