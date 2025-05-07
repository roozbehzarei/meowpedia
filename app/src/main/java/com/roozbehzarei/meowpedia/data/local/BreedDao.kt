package com.roozbehzarei.meowpedia.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BreedDao {

    @Query("SELECT * FROM breed")
    fun getAllBreeds(): List<Breed>

    @Query("UPDATE breed SET is_favorite = NOT is_favorite WHERE id = :id")
    fun toggleFavorite(id: String)

}