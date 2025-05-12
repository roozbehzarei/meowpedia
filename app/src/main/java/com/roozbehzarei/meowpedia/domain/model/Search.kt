package com.roozbehzarei.meowpedia.domain.model

/**
 * Represents the data model behind searching for specific breeds.
 *
 * @property isLoading Indicates if the search operation is currently in progress.
 * @property result The list of [Breed] objects that match the search criteria. This list will be empty
 * if the search is still loading or no results were found.
 */
data class Search(val isLoading: Boolean, val result: List<Breed>)
