package com.roozbehzarei.meowpedia.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.roozbehzarei.meowpedia.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Delegated property to create and access a Preferences DataStore named "settings" on Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Implementation of [UserPreferencesRepository] using Jetpack DataStore for storing user settings.
 *
 * Provides flows for observing preference values and suspend functions for updating them.
 */
class UserPreferencesRepositoryImpl(private val context: Context) : UserPreferencesRepository {

    private companion object {
        val IS_DYNAMIC_COLORS = booleanPreferencesKey("is_dynamic_colors")
        val THEME_MODE = intPreferencesKey("theme_mode")
    }

    override fun getDynamicColorsPreference(): Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[IS_DYNAMIC_COLORS] == true
        }

    override fun getThemePreference(): Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: 1
    }

    /**
     * Persists the user's dynamic colors preference.
     *
     * @param isDynamic `true` to enable dynamic colors, `false` to disable
     */
    override suspend fun saveDynamicColorsPreference(isDynamic: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DYNAMIC_COLORS] = isDynamic
        }
    }

    /**
     * Persists the user's selected theme mode index.
     *
     * @param index Integer representing the theme mode (`0` = Light, `2` = Dark, etc = Default)
     */
    override suspend fun saveThemeModePreference(index: Int) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = index
        }
    }

}