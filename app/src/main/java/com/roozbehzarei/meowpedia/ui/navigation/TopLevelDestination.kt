package com.roozbehzarei.meowpedia.ui.navigation

import com.roozbehzarei.meowpedia.R
import kotlinx.serialization.Serializable

@Serializable
object MainRoute

/**
 * @property id Unique identifier of the breed to display details for
 */
@Serializable
data class BreedDetailsRoute(val id: String)

@Serializable
object SettingsRoute

/**
 * Enumerates the top-level destinations for use in app bar or drawer.
 *
 * @param labelResource Resource ID for the display label
 * @param route Serializable route object or data class for navigation
 */
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