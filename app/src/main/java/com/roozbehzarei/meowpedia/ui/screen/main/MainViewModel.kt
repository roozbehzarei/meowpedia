package com.roozbehzarei.meowpedia.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.roozbehzarei.meowpedia.BuildConfig
import com.roozbehzarei.meowpedia.data.local.entity.BreedEntity
import com.roozbehzarei.meowpedia.domain.repository.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    pager: Pager<Int, BreedEntity>, private val breedRepository: BreedRepository
) : ViewModel() {

    val breedPagingFlow = pager.flow.map { pagingData ->
        pagingData.map { it.toBreed() }
    }.cachedIn(viewModelScope)

    fun getBreedImage(key: String) {
        viewModelScope.launch {
            with(Dispatchers.IO) {
                try {
                    breedRepository.getImage(key)
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) e.printStackTrace()
                }
            }
        }
    }
}