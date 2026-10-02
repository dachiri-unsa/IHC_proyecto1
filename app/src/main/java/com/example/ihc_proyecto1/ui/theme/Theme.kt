package com.example.ihc_proyecto1.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

/**
 * La app es de marca clara (guinda + blanco), por eso el tema es siempre claro
 * y NO usa el color dinámico del sistema: el guinda debe verse siempre igual.
 */
private val ColorSchemeClaro = lightColorScheme(
    primary = Guinda,
    onPrimary = Blanco,
    primaryContainer = GuindaContainer,
    onPrimaryContainer = GuindaOnContainer,
    secondary = GuindaOscuro,
    onSecondary = Blanco,
    secondaryContainer = GuindaContainer,
    onSecondaryContainer = GuindaOnContainer,
    tertiary = GuindaClaro,
    onTertiary = Blanco,
    background = FondoApp,
    onBackground = TextoPrimario,
    surface = Superficie,
    onSurface = TextoPrimario,
    surfaceVariant = Color(0xFFF6EDED),
    onSurfaceVariant = TextoSecundario,
    outline = Borde,
    outlineVariant = Color(0xFFF0E3E3),
    error = ColorError,
    onError = Blanco,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B)
)

@Composable
fun IHC_proyecto1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.navigationBarColor = Superficie.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = ColorSchemeClaro,
        typography = Typography,
        content = content
    )
}