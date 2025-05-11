package com.roozbehzarei.meowpedia.ui.screen.main

import com.roozbehzarei.meowpedia.domain.model.Favorite
import com.roozbehzarei.meowpedia.domain.model.Search

data class MainUiState(
    val favoriteItems: List<Favorite>,
    val isSearchMode: Boolean,
    val search: Search
)