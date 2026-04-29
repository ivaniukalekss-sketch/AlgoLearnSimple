package com.ivaniuk.algolearnsimple.domain.usecase

import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: AlgorithmRepository
) {
    suspend operator fun invoke(algorithmId: Int) {
        repository.toggleFavorite(algorithmId)
    }
}