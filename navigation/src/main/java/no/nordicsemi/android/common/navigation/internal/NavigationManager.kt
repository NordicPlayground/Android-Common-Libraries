package no.nordicsemi.android.common.navigation.internal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import no.nordicsemi.android.common.navigation.DestinationId
import no.nordicsemi.android.common.navigation.Navigator
import javax.inject.Inject

internal const val RESULT_KEY = "result"

@Parcelize
internal object Cancelled : Parcelable
@Parcelize
private object Initial : Parcelable

/**
 * A navigation manager that can be used to navigate to next destination, or back.
 *
 * @property executor The [NavigationExecutor] that will perform the navigation.
 * @property currentDestinationId The identifier of the current destination.
 */
@ActivityRetainedScoped
internal class NavigationManager @Inject constructor(
    @ApplicationContext private val context: Context,
): Navigator {
    var executor: NavigationExecutor? = null
    var currentDestinationId: DestinationId? = null
    var savedStateHandle: SavedStateHandle? = null

    override fun navigate(hint: Any?, args: Bundle?) {
        currentDestinationId?.let { here ->
            executor?.navigate(
                from = here,
                hint = hint,
                args = args,
            )
        }
    }

    override suspend fun <T> navigateForResult(hint: Any?, args: Bundle?): T? =
        withContext(Dispatchers.Main) {
            savedStateHandle?.run {
                // Make sure the executor is set.
                if (executor == null) return@run null
                // Create a result flow.
                getStateFlow<Any?>(RESULT_KEY, Initial)
                    // Drop the initial value.
                    .drop(1)
                    // Navigate using the executor.
                    .also { navigate(hint, args) }
                    // Suspend until the result is received.
                    .run { firstOrNull() }
                    // Remove the result from the SavedStateHandle.
                    .also { remove<Any?>("result") }
                    // Convert to T?
                    .let {
                        @Suppress("UNCHECKED_CAST")
                        when (it) {
                            is Cancelled -> null
                            else -> it as? T
                        }
                    }
            }
        }

    override fun navigateUp() {
        executor?.navigateUp()
    }

    override fun navigateUpWithResult(result: Any) {
        executor?.navigateUpWithResult(result)
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