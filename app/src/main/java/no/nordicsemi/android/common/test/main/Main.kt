package no.nordicsemi.android.common.test.view

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.logger.view.LoggerAppBarIcon
import no.nordicsemi.android.common.navigation.DestinationId
import no.nordicsemi.android.common.navigation.NavigationDestination
import no.nordicsemi.android.common.navigation.NavigationDestinations
import no.nordicsemi.android.common.navigation.Navigator
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.test.view.page.*
import no.nordicsemi.android.common.theme.view.NordicAppBar
import no.nordicsemi.android.common.theme.view.PagerView
import no.nordicsemi.android.common.theme.view.PagerViewEntity

val Main = DestinationId("main")

/**
 * List of destinations defined in the module.
 *
 * Optionally, this can define a local Router for routing navigation within the module.
 */
val MainDestinations = NavigationDestinations(listOf(
    NavigationDestination(Main) { navigator ->
        MainScreen(navigator)
    },
))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(navigator: Navigator) {
    Column {
        NordicAppBar(
            text = stringResource(id = R.string.title_main),
            actions = {
                val context = LocalContext.current
                LoggerAppBarIcon(
                    onClick = {
                        Toast.makeText(context, "Logger clicked", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
        val pages = PagerViewEntity(listOf(
            BasicsPage,
            Fonts,
            Wizard,
            Connection,
            Warning,
        ))
        PagerView(
            viewEntity = pages,
            contentPadding = PaddingValues(horizontal = 16.dp),
            itemSpacing = 16.dp,
        )
    }
}