package com.roozbehzarei.meowpedia.data.mapper

import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity
import com.roozbehzarei.meowpedia.domain.model.Breed

/**
 * Converts a [BreedEntity] object to a [Breed] object.
 *
 * This extension function maps the properties from a database entity representation of a breed
 * to its domain model representation.
 *
 * @return The converted [Breed] object.
 */
fun BreedEntity.toBreed(): Breed = Breed(
    id = id,
    name = name,
    temperament = temperament,
    origin = origin,
    description = description,
    lifeSpan = lifeSpan,
    wikipediaUrl = wikipediaUrl,
    imageId = imageId,
    imageUrl = imageUrl
)

/**
 * Converts a [Breed] domain model to a [BreedEntity] local database entity.
 * @return A [BreedEntity] object representing the breed for local database storage.
 */
fun Breed.toEntity(): BreedEntity = BreedEntity(
    id = id,
    name = name,
    temperament = temperament,
    origin = origin,
    description = description,
    lifeSpan = lifeSpan,
    wikipediaUrl = wikipediaUrl,
    imageId = imageId,
    imageUrl = imageUrl
)
