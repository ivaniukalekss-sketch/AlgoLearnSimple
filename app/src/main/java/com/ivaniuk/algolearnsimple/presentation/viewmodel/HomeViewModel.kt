package com.ivaniuk.algolearnsimple.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivaniuk.algolearnsimple.domain.model.Algorithm
import com.ivaniuk.algolearnsimple.domain.usecase.GetAlgorithmsUseCase
import com.ivaniuk.algolearnsimple.domain.usecase.ToggleFavoriteUseCase
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAlgorithmsUseCase: GetAlgorithmsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val repository: AlgorithmRepository  // ← ДОБАВИТЬ
) : ViewModel() {

    val algorithms = getAlgorithmsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Для текущего алгоритма
    private val _currentAlgorithm = MutableStateFlow<Algorithm?>(null)
    val currentAlgorithm: StateFlow<Algorithm?> = _currentAlgorithm.asStateFlow()

    fun toggleFavorite(algorithmId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(algorithmId)
        }
    }

    fun loadAlgorithmById(id: Int) {
        viewModelScope.launch {
            println("🔥 loadAlgorithmById: загружаем алгоритм $id")
            val algorithm = repository.getAlgorithmById(id)
            _currentAlgorithm.value = algorithm
            println("🔥 loadAlgorithmById: загружен ${algorithm?.title}")
        }
    }
}