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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val view = this@MainActivity.findViewById<View>(android.R.id.content)
        val insetsController = WindowCompat.getInsetsController(this@MainActivity.window, view)
        setContent {
            val themeMode by viewModel.themeMode.collectAsState(1)
            val isDynamicColor by viewModel.isDynamicColor.collectAsState(false)
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