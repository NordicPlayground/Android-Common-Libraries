package no.nordicsemi.android.navigation

import android.app.Activity
import androidx.navigation.NavHostController
import java.lang.ref.WeakReference

data class NavigationWrapper(
    val activity: WeakReference<Activity>,
    val navController: NavHostController,
) {

    constructor(activity: Activity, navController: NavHostController) : this(WeakReference(activity), navController)

    fun consumeEvent(destination: ConsumableNavigationDestination) {
        if (destination.isConsumed) {
            return
        }
        when (val dest = destination.destination) {
            BackDestination -> navigateBack()
            is ForwardDestination -> navController.navigate(dest.id.name)
            InitialDestination -> doNothing() //Needed because collectAsState() requires initial state.
        }
    }

    private fun navigateBack() {
        if (navController.backQueue.size > 2) {
            navController.navigateUp()
        } else {
            activity.get()?.finish()
        }
    }
}
