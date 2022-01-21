package no.nordicsemi.android.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationView(destinations: ComposeDestinations) {
    val navController = rememberNavController()
    val viewModel: NavigationViewModel = hiltViewModel()
    BackHandler { viewModel.navigateUp() }

    val destination = viewModel.destination.collectAsState()

    NavHost(
        navController = navController,
        startDestination = destinations.values[0].id.name
    ) {
        destinations.values.forEach { destination ->
            composable(destination.id.name) { destination.draw() }
        }
    }

    val context = LocalContext.current as Activity
    LaunchedEffect(destination.value) {
        if (destination.value.isConsumed) {
            return@LaunchedEffect
        }
        when (val dest = destination.value.destination) {
            BackDestination -> navController.navigateUp()
            FinishDestination -> context.finish()
            is ForwardDestination -> navController.navigate(dest.id.name)
            InitialDestination -> doNothing() //Needed because collectAsState() requires initial state.
        }
        viewModel.consumeLastEvent()
    }
}
