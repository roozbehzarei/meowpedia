package com.roozbehzarei.meowpedia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Breed::class], version = 1)
abstract class BreedDatabase : RoomDatabase() {

    abstract fun breedDao(): BreedDao

}