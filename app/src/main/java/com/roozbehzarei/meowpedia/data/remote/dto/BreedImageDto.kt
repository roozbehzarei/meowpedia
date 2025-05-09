package com.roozbehzarei.meowpedia.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BreedImageDto(
    val id: String,
    val url: String?
)