package com.roozbehzarei.meowpedia.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roozbehzarei.meowpedia.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val isDynamicColor = userPreferencesRepository.getDynamicColorsPreference()
    val themeMode = userPreferencesRepository.getThemePreference()

    fun saveDynamicColorsPreference(isEnabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveDynamicColorsPreference(isEnabled)
        }
    }

    fun saveThemeModePreference(index: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveThemeModePreference(index)
        }
    }

}