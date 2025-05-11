package com.roozbehzarei.meowpedia.domain.repository

import com.roozbehzarei.meowpedia.domain.model.Breed

interface BreedRepository {
    suspend fun getImage(key: String)
    suspend fun getBreedDetails(id: String): Breed
    suspend fun searchBreeds(name: String): List<Breed>
}