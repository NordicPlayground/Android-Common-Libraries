package no.nordicsemi.android.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationView(destinations: ComposeDestinations) {
    val navController = rememberNavController()
    val viewModel: NavigationViewModel = hiltViewModel()
    viewModel.navController = navController

    val activity = LocalContext.current as Activity
    BackHandler {
        if (navController.backQueue.size > 2) {
            viewModel.navigateUp()
        } else {
            activity.finish()
        }
    }

    NavHost(
        navController = navController,
        startDestination = destinations.values[0].id.name
    ) {
        destinations.values.forEach { destination ->
            composable(destination.id.name) { destination.draw() }
        }
    }
}
