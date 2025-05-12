package com.roozbehzarei.meowpedia.data.mapper

import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity
import com.roozbehzarei.meowpedia.domain.model.Breed

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
