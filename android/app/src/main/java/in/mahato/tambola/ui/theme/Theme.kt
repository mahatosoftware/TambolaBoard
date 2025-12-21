package `in`.mahato.tambola.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppLightColors = lightColorScheme(
    primary = PurpleBg,
    onPrimary = WhiteText,
    primaryContainer = Black,
    onPrimaryContainer = WhiteText,

    background = WhiteBg,
    onBackground = BlackText,



    surface = PurpleBg,
    onSurface = WhiteText,

    secondary=PurpleDark,
    onSecondary = WhiteText,

    tertiary = WhiteBg,
    onTertiary = BlackText,

    secondaryContainer = Byzantium,

    tertiaryContainer = Bronze





)

private val AppDarkColors = AppLightColors

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) AppDarkColors else AppLightColors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
