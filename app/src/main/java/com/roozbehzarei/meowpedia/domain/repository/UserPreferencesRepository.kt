package com.roozbehzarei.meowpedia.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getDynamicColorsPreference(): Flow<Boolean>
    fun getThemePreference(): Flow<Int>
    suspend fun saveDynamicColorsPreference(isDynamic: Boolean)
    suspend fun saveThemeModePreference(index: Int)
}