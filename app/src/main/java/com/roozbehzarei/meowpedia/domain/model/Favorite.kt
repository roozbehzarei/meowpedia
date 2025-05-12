package com.roozbehzarei.meowpedia.domain.model

/**
 * Represents a favorite status for a cat breed.
 *
 * @property id The unique identifier of the cat breed.
 * @property isFavorite A boolean indicating whether the breed is a favorite. `null` if the favorite status is not yet assigned.
 */
data class Favorite(val id: String, val isFavorite: Boolean?)