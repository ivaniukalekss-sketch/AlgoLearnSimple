package com.ivaniuk.algolearnsimple.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: AlgorithmRepository,
) : ViewModel() {

    val algorithms = repository.getAllAlgorithms()
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

}