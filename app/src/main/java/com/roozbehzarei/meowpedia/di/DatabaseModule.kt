package com.roozbehzarei.meowpedia.di

import android.content.Context
import androidx.room.Room
import com.roozbehzarei.meowpedia.data.local.BreedDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
}