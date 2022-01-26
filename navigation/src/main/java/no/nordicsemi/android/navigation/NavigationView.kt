package no.nordicsemi.android.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationView(destinations: ComposeDestinations) {
    val navController = rememberNavController()
    val viewModel: NavigationViewModel = hiltViewModel()
    viewModel.navController = navController
    BackHandler { viewModel.navigateUp() }

    NavHost(
        navController = navController,
        startDestination = destinations.values[0].id.name
    ) {
        destinations.values.forEach { destination ->
            composable(destination.id.name) { destination.draw() }
        }
    }
}
