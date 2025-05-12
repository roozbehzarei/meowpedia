package com.roozbehzarei.meowpedia.di

import android.content.Context
import com.roozbehzarei.meowpedia.data.repository.UserPreferencesRepositoryImpl
import com.roozbehzarei.meowpedia.domain.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module responsible for providing preference-related dependencies.
 *
 * Installed in the [SingletonComponent] for application-wide singletons.
 */
@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context
    ): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(context = context)
    }

}