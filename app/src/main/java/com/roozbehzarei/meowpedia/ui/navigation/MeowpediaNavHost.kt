package com.roozbehzarei.meowpedia.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.roozbehzarei.meowpedia.ui.screen.breed_details.BreedDetailsScreen
import com.roozbehzarei.meowpedia.ui.screen.breed_list.MainScreen
import com.roozbehzarei.meowpedia.ui.screen.settings.SettingsScreen
import kotlinx.coroutines.launch

@Composable
fun MeowpediaNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {

    val coroutineScope = rememberCoroutineScope()

    return NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TopLevelDestination.Main.route
    ) {
        composable<MainRoute> {
            MainScreen(
                onBreedClicked = { id -> navController.navigate(BreedDetailsRoute(id)) },
                onShowMessage = { coroutineScope.launch { snackbarHostState.showSnackbar(it) } })
        }
        composable<BreedDetailsRoute> { backStackEntry ->
            val breedDetailsRoute: BreedDetailsRoute = backStackEntry.toRoute()
            BreedDetailsScreen(id = breedDetailsRoute.id)
        }
        composable<SettingsRoute> {
            SettingsScreen()
        }
    }

}