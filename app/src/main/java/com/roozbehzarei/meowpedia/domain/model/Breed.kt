package com.roozbehzarei.meowpedia.domain.model

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