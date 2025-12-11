package com.ivaniuk.algolearnsimple.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import com.ivaniuk.algolearnsimple.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: AlgorithmRepository,
    private val statisticsRepository: StatisticsRepository? = null
) : ViewModel() {

    val algorithms = repository.getAllAlgorithms()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun trackAlgorithmView(algorithmId: Int, algorithmName: String) {
        statisticsRepository?.let {
            viewModelScope.launch {
                it.incrementAlgorithmView(algorithmId, algorithmName)
            }
        }
    }

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