package com.roozbehzarei.meowpedia.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    pager: Pager<Int, BreedEntity>
) : ViewModel() {

    val breedPagingFlow = pager.flow.map { pagingData ->
        pagingData.map { it.toBreed() }
    }.cachedIn(viewModelScope)

}