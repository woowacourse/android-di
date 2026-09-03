package woowacourse.shopping.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme =
    lightColorScheme(
        primary = Purple500,
        primaryContainer = Purple700,
        secondary = Teal200,
        secondaryContainer = Teal700,
    )

private val DarkColorScheme =
    darkColorScheme(
        primary = Purple200,
        primaryContainer = Purple700,
        secondary = Teal200,
        secondaryContainer = Teal200,
    )

@Composable
fun ShoppingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = ShoppingTypography,
        content = content,
    )
}
