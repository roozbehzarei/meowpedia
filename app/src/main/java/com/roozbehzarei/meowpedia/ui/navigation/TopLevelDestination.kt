package com.roozbehzarei.meowpedia.ui.navigation

import com.roozbehzarei.meowpedia.R
import kotlinx.serialization.Serializable

@Serializable
object MainRoute

@Serializable
data class BreedDetailsRoute(val id: String)

@Serializable
object SettingsRoute

enum class TopLevelDestination(
    val labelResource: Int, val route: Any
) {
    Main(
        labelResource = R.string.app_name, route = MainRoute
    ),
    BreedDetails(
        labelResource = R.string.empty, route = BreedDetailsRoute
    ),
    Settings(
        labelResource = R.string.settings_title, route = SettingsRoute
    )

}