package com.example.evention.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.evention.R

val arialFontFamily = FontFamily(
    Font(R.font.arial)
)

val CustomTypography = Typography(
    displayLarge = TextStyle(fontFamily = arialFontFamily, fontSize = 57.sp),
    displayMedium = TextStyle(fontFamily = arialFontFamily, fontSize = 45.sp),
    displaySmall = TextStyle(fontFamily = arialFontFamily, fontSize = 36.sp),
    headlineLarge = TextStyle(fontFamily = arialFontFamily, fontSize = 32.sp),
    headlineMedium = TextStyle(fontFamily = arialFontFamily, fontSize = 28.sp),
    headlineSmall = TextStyle(fontFamily = arialFontFamily, fontSize = 24.sp),
    titleLarge = TextStyle(fontFamily = arialFontFamily, fontSize = 22.sp),
    titleMedium = TextStyle(fontFamily = arialFontFamily, fontSize = 16.sp),
    titleSmall = TextStyle(fontFamily = arialFontFamily, fontSize = 14.sp),
    bodyLarge = TextStyle(fontFamily = arialFontFamily, fontSize = 16.sp),
    bodyMedium = TextStyle(fontFamily = arialFontFamily, fontSize = 14.sp),
    bodySmall = TextStyle(fontFamily = arialFontFamily, fontSize = 12.sp),
    labelLarge = TextStyle(fontFamily = arialFontFamily, fontSize = 14.sp),
    labelMedium = TextStyle(fontFamily = arialFontFamily, fontSize = 12.sp),
    labelSmall = TextStyle(fontFamily = arialFontFamily, fontSize = 11.sp),
)

private val DarkColorScheme = darkColorScheme(
    primary = EventionBlue,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = EventionBlue,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun EventionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        content = content
    )
}
