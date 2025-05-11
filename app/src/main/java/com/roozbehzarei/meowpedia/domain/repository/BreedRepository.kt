package com.roozbehzarei.meowpedia.domain.repository

import com.roozbehzarei.meowpedia.domain.model.Breed
import kotlinx.coroutines.flow.Flow

interface BreedRepository {
    suspend fun getImage(key: String)
    suspend fun getBreedDetails(id: String): Flow<Breed>
    suspend fun searchBreeds(name: String): List<Breed>
}