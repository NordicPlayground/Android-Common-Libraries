package no.nordicsemi.ui.scanner.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.ui.scanner.R

@Composable
fun AppBar(text: String, showProgress: Boolean = false, onNavigationButtonClick: () -> Unit) {

    SmallTopAppBar(
        title = { Text(text) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.primary,
            containerColor = colorResource(id = R.color.appBarColor),
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            IconButton(onClick = { onNavigationButtonClick() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigation_item_accessibility),
                )
            }
        },
        actions = {
            if (showProgress) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(horizontal = 16.dp).size(30.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
    )
}

@Preview
@Composable
private fun AppBarPreview() {
    AppBar(text = "Test") { }
}
