package com.roozbehzarei.meowpedia.domain.model

/**
 * Represents a breed of cat with various characteristics.
 *
 * @property id The unique identifier for the breed.
 * @property name The name of the breed.
 * @property temperament A description of the breed's typical temperament.
 * @property origin The origin of the breed.
 * @property description A detailed description of the breed.
 * @property lifeSpan The typical lifespan of the breed.
 * @property wikipediaUrl The URL of the Wikipedia page for the breed (nullable).
 * @property imageId The ID of the breed's image (nullable).
 * @property imageUrl The URL of the breed's image (nullable).
 */
data class Breed(
    val id: String,
    val name: String,
    val temperament: String,
    val origin: String,
    val description: String,
    val lifeSpan: String,
    val wikipediaUrl: String?,
    val imageId: String?,
    val imageUrl: String?
)