package com.roozbehzarei.meowpedia.ui.screen.breed_details

import com.roozbehzarei.meowpedia.domain.model.Breed
import kotlinx.coroutines.Job

data class BreedDetailsUiState(
    val breed: Breed?, val isFavorite: Boolean?, val updateFavoriteJob: Job?
)