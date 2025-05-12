package com.roozbehzarei.meowpedia.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.roozbehzarei.meowpedia.ui.theme.MeowpediaTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry activity for the Meowpedia app.
 *
 * Hosts the Compose UI, installs the splash screen, and sets up window insets
 * and dynamic theming based on user preferences.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Retrieve the root content view for controlling window insets
        val view = this@MainActivity.findViewById<View>(android.R.id.content)
        val insetsController = WindowCompat.getInsetsController(this@MainActivity.window, view)
        // Set up the Compose UI content
        setContent {
            val themeMode by viewModel.themeMode.collectAsState(1)
            val isDynamicColor by viewModel.isDynamicColor.collectAsState(false)
            // Adjust status bar icon color based on themeMode:
            // 0 = Light, 1 = Follow system, 2 = Dark
            when (themeMode) {
                0 -> insetsController.isAppearanceLightStatusBars = true
                2 -> insetsController.isAppearanceLightStatusBars = false
                else -> insetsController.isAppearanceLightStatusBars = isSystemInDarkTheme().not()
            }
            MeowpediaTheme(
                dynamicColor = isDynamicColor, darkTheme = when (themeMode) {
                    0 -> false
                    2 -> true
                    else -> isSystemInDarkTheme()
                }
            ) {
                MeowpediaApp(modifier = Modifier.Companion.fillMaxSize())
            }
        }
    }

}