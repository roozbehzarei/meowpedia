package com.roozbehzarei.meowpedia.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing user preferences, such as theming and dynamic colors.
 */
interface UserPreferencesRepository {
    fun getDynamicColorsPreference(): Flow<Boolean>
    fun getThemePreference(): Flow<Int>
    suspend fun saveDynamicColorsPreference(isDynamic: Boolean)
    suspend fun saveThemeModePreference(index: Int)
}