package no.nordicsemi.android.common.navigation.internal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import no.nordicsemi.android.common.navigation.DestinationId
import no.nordicsemi.android.common.navigation.Navigator
import javax.inject.Inject

internal data class NavigationTarget(
    val destination: DestinationId<*, *>,
    val args: @RawValue Any?
) {
    fun toBundle() = args?.let { bundleOf(destination.name to args) }
}

internal sealed class NavigationResult
@Parcelize
internal object Initial : NavigationResult(), Parcelable
@Parcelize
internal object Cancelled : NavigationResult(), Parcelable
@Parcelize
internal class Success(val value: @RawValue Any) : NavigationResult(), Parcelable

/**
 * A navigation manager that can be used to navigate to next destination, or back.
 *
 * @param context the application context.
 * @property executor The [NavigationExecutor] that will perform the navigation.
 * @property savedStateHandle The [SavedStateHandle] that will be used to store the navigation
 * result.
 */
@ActivityRetainedScoped
internal class NavigationManager @Inject constructor(
    @ApplicationContext private val context: Context,
): Navigator {
    internal var executor: NavigationExecutor? = null
    internal var savedStateHandle: SavedStateHandle? = null

    override fun <R> resultFrom(from: DestinationId<*, R>): Flow<R?> =
        @Suppress("UNCHECKED_CAST")
        savedStateHandle?.run {
            getStateFlow<NavigationResult>(from.name, Initial)
                .transform { result ->
                    when (result) {
                        // Ignore the initial value.
                        is Initial -> {}
                        // Return success result.
                        is Success -> emit(result.value as R)
                        // Return null when cancelled.
                        is Cancelled -> emit(null)
                    }
                }
        } ?: throw IllegalStateException("SavedStateHandle is not set")

    override fun <A> navigateTo(to: DestinationId<A, *>, args: A?) {
        executor?.navigate(NavigationTarget(to, args))
    }

    override fun <R> navigateUpWithResult(from: DestinationId<*, R>, result: R) {
        executor?.navigateUpWithResult(Success(result as Any))
    }

    override fun navigateUp() {
        executor?.navigateUpWithResult(Cancelled)
    }

    override fun open(link: Uri) {
        try {
            with (Intent(Intent.ACTION_VIEW, link)) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(this)
            }
        } catch (e: Exception) {
            Log.e("Navigator", "Failed to open link: $link", e)
        }
    }
}