package com.roozbehzarei.meowpedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.roozbehzarei.meowpedia.ui.MeowpediaApp
import com.roozbehzarei.meowpedia.ui.theme.MeowpediaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeowpediaTheme {
                MeowpediaApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}