package com.ivaniuk.algolearnsimple.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivaniuk.algolearnsimple.domain.usecase.GetAlgorithmsUseCase
import com.ivaniuk.algolearnsimple.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAlgorithmsUseCase: GetAlgorithmsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    val algorithms = getAlgorithmsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavorite(algorithmId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(algorithmId)
        }
    }
}