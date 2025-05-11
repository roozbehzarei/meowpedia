package com.roozbehzarei.meowpedia.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.roozbehzarei.meowpedia.data.local.dao.BreedDao
import com.roozbehzarei.meowpedia.data.local.dao.FavoriteDao
import com.roozbehzarei.meowpedia.data.local.dao.RemoteKeyDao
import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity
import com.roozbehzarei.meowpedia.data.local.entity.FavoriteEntity
import com.roozbehzarei.meowpedia.data.local.entity.RemoteKeyEntity

@Database(
    entities = [BreedEntity::class, RemoteKeyEntity::class, FavoriteEntity::class],
    version = 1
)
abstract class BreedDatabase : RoomDatabase() {

    abstract fun breedDao(): BreedDao

    abstract fun remoteKeyDao(): RemoteKeyDao

    abstract fun favoriteDao(): FavoriteDao

}