package no.nordicsemi.android.common.test.view

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.logger.view.LoggerAppBarIcon
import androidx.compose.material3.ExperimentalMaterial3Api
import no.nordicsemi.android.common.navigation.ComposeDestination
import no.nordicsemi.android.common.navigation.ComposeDestinations
import no.nordicsemi.android.common.navigation.DestinationId
import no.nordicsemi.android.common.navigation.NavigationManager
import no.nordicsemi.android.common.test.R
import no.nordicsemi.android.common.test.view.page.*
import no.nordicsemi.android.common.theme.view.NordicAppBar
import no.nordicsemi.android.common.theme.view.PagerView
import no.nordicsemi.android.common.theme.view.PagerViewEntity

val Main = DestinationId("main")

val mainDestinations = ComposeDestinations(listOf(
    ComposeDestination(Main) { navigationManager ->
        MainScreen(navigationManager)
    },
))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navigationManager: NavigationManager,
) {
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