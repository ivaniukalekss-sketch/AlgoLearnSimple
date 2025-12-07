package com.ivaniuk.algolearnsimple.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: AlgorithmRepository
) : ViewModel() {

    val algorithms = repository.getAllAlgorithms()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favoriteAlgorithms = repository.getAllAlgorithms()
        .map { algorithms -> algorithms.filter { it.isFavorite } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavorite(algorithmId: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(algorithmId)
        }
    }

    suspend fun getAlgorithmById(id: Int) = repository.getAlgorithmById(id)
}