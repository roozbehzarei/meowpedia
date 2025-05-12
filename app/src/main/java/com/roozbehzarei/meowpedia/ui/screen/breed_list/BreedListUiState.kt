package com.roozbehzarei.meowpedia.ui.screen.breed_list

import com.roozbehzarei.meowpedia.domain.model.Favorite
import com.roozbehzarei.meowpedia.domain.model.Search

data class BreedListUiState(
    val favoriteItems: List<Favorite>,
    val isSearchMode: Boolean,
    val search: Search
)