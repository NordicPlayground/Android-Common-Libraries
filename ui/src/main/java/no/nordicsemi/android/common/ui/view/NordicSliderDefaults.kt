package no.nordicsemi.android.common.ui.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable

/**
 * Object to hold defaults Nordic colors used by Slider.
 */
object NordicSliderDefaults {

    @Composable
    fun colors(): SliderColors = SliderDefaults.colors(
        activeTickColor = MaterialTheme.colorScheme.onPrimary,
        inactiveTickColor = MaterialTheme.colorScheme.primary,
        activeTrackColor = MaterialTheme.colorScheme.primary,
        inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
    )
}