package com.roozbehzarei.meowpedia.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.roozbehzarei.meowpedia.ui.navigation.BreedDetailsRoute
import com.roozbehzarei.meowpedia.ui.navigation.MeowpediaNavHost
import com.roozbehzarei.meowpedia.ui.navigation.SettingsRoute
import com.roozbehzarei.meowpedia.ui.navigation.TopLevelDestination

@Composable
fun MeowpediaApp(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Find matching top-level destination for current route
    val snackbarHostState = SnackbarHostState()
    // Find matching top-level destination for current route
    val currentDestination = when (backStackEntry?.destination?.route.orEmpty()) {
        BreedDetailsRoute::class.qualifiedName -> TopLevelDestination.BreedDetails
        SettingsRoute::class.qualifiedName -> TopLevelDestination.Settings
        else -> TopLevelDestination.Main
    }

    Scaffold(modifier, snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = {
        TopBar(
            title = stringResource(currentDestination.labelResource),
            canNavigateUp = (currentDestination == TopLevelDestination.Main).not(),
            onNavigateUp = { navController.navigateUp() },
            onNavigateToSettings = { navController.navigate(TopLevelDestination.Settings.route) },
        )
    }) { innerPadding ->
        MeowpediaNavHost(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    title: String,
    canNavigateUp: Boolean,
    onNavigateUp: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    CenterAlignedTopAppBar(title = { Text(title) }, actions = {
        IconButton(
            onClick = onNavigateToSettings
        ) {
            Icon(Icons.Filled.Settings, null)
        }
    }, navigationIcon = {
        if (canNavigateUp) {
            IconButton(onClick = onNavigateUp) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        }
    })
}