package com.roozbehzarei.meowpedia.di

import android.content.Context
import androidx.room.Room
import com.roozbehzarei.meowpedia.data.local.db.BreedDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database related dependencies.
 * It is installed in the [SingletonComponent], ensuring that the provided dependencies are
 * singleton instances throughout the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBreedDatabase(@ApplicationContext context: Context): BreedDatabase {
        return Room.databaseBuilder(context, BreedDatabase::class.java, "breed_db")
            .fallbackToDestructiveMigration(true).build()
    }

    @Provides
    @Singleton
    fun provideBreedDao(db: BreedDatabase) = db.breedDao()

    @Provides
    @Singleton
    fun provideRemoteKeyDao(db: BreedDatabase) = db.remoteKeyDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(db: BreedDatabase) = db.favoriteDao()

}