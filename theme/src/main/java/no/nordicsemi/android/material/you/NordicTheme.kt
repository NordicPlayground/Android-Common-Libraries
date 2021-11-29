package no.nordicsemi.android.material.you

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource

@Composable
fun NordicTheme(content: @Composable () -> Unit) {
    val darkColorPalette = darkColorScheme(
        primary = colorResource(id = R.color.md_theme_primary),
        onPrimary = colorResource(id = R.color.md_theme_onPrimary),
        primaryContainer = colorResource(id = R.color.md_theme_primaryContainer),
        onPrimaryContainer = colorResource(id = R.color.md_theme_onPrimaryContainer),
        inversePrimary = colorResource(id = R.color.md_theme_primaryInverse),
        secondary = colorResource(id = R.color.md_theme_secondary),
        onSecondary = colorResource(id = R.color.md_theme_onSecondary),
        secondaryContainer = colorResource(id = R.color.md_theme_secondaryContainer),
        onSecondaryContainer = colorResource(id = R.color.md_theme_onSecondaryContainer),
        tertiary = colorResource(id = R.color.md_theme_tertiary),
        onTertiary = colorResource(id = R.color.md_theme_onTertiary),
        tertiaryContainer = colorResource(id = R.color.md_theme_tertiaryContainer),
        onTertiaryContainer = colorResource(id = R.color.md_theme_onTertiaryContainer),
        background = colorResource(id = R.color.md_theme_background),
        onBackground = colorResource(id = R.color.md_theme_onBackground),
        surface = colorResource(id = R.color.md_theme_surface),
        onSurface = colorResource(id = R.color.md_theme_onSurface),
        surfaceVariant = colorResource(id = R.color.md_theme_surfaceVariant),
        onSurfaceVariant = colorResource(id = R.color.md_theme_onSurfaceVariant),
        inverseSurface = colorResource(id = R.color.md_theme_inverseSurface),
        inverseOnSurface = colorResource(id = R.color.md_theme_inverseOnSurface),
        error = colorResource(id = R.color.md_theme_error),
        onError = colorResource(id = R.color.md_theme_onError),
        errorContainer = colorResource(id = R.color.md_theme_errorContainer),
        onErrorContainer = colorResource(id = R.color.md_theme_onErrorContainer),
        outline = colorResource(id = R.color.md_theme_outline),
    )

    val lightColorPalette = lightColorScheme(
        primary = colorResource(id = R.color.md_theme_primary),
        onPrimary = colorResource(id = R.color.md_theme_onPrimary),
        primaryContainer = colorResource(id = R.color.md_theme_primaryContainer),
        onPrimaryContainer = colorResource(id = R.color.md_theme_onPrimaryContainer),
        inversePrimary = colorResource(id = R.color.md_theme_primaryInverse),
        secondary = colorResource(id = R.color.md_theme_secondary),
        onSecondary = colorResource(id = R.color.md_theme_onSecondary),
        secondaryContainer = colorResource(id = R.color.md_theme_secondaryContainer),
        onSecondaryContainer = colorResource(id = R.color.md_theme_onSecondaryContainer),
        tertiary = colorResource(id = R.color.md_theme_tertiary),
        onTertiary = colorResource(id = R.color.md_theme_onTertiary),
        tertiaryContainer = colorResource(id = R.color.md_theme_tertiaryContainer),
        onTertiaryContainer = colorResource(id = R.color.md_theme_onTertiaryContainer),
        background = colorResource(id = R.color.md_theme_background),
        onBackground = colorResource(id = R.color.md_theme_onBackground),
        surface = colorResource(id = R.color.md_theme_surface),
        onSurface = colorResource(id = R.color.md_theme_onSurface),
        surfaceVariant = colorResource(id = R.color.md_theme_surfaceVariant),
        onSurfaceVariant = colorResource(id = R.color.md_theme_onSurfaceVariant),
        inverseSurface = colorResource(id = R.color.md_theme_inverseSurface),
        inverseOnSurface = colorResource(id = R.color.md_theme_inverseOnSurface),
        error = colorResource(id = R.color.md_theme_error),
        onError = colorResource(id = R.color.md_theme_onError),
        errorContainer = colorResource(id = R.color.md_theme_errorContainer),
        onErrorContainer = colorResource(id = R.color.md_theme_onErrorContainer),
        outline = colorResource(id = R.color.md_theme_outline),
    )

    val colorScheme = if (isSystemInDarkTheme()) {
        darkColorPalette
    } else {
        lightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = nordicTypography,
        content = content
    )
}
