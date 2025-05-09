package com.roozbehzarei.meowpedia.ui

import androidx.lifecycle.ViewModel
import com.roozbehzarei.meowpedia.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val isDynamicColor = userPreferencesRepository.getDynamicColorsPreference()
    val themeMode = userPreferencesRepository.getThemePreference()

}