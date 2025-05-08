package com.roozbehzarei.meowpedia.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BreedImageDto(
    val url: String?
)