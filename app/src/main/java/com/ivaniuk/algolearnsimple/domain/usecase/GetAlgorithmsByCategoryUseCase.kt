package com.ivaniuk.algolearnsimple.domain.usecase

import com.ivaniuk.algolearnsimple.domain.model.Algorithm
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlgorithmsByCategoryUseCase @Inject constructor(
    private val repository: AlgorithmRepository
) {
    operator fun invoke(category: String): Flow<List<Algorithm>> =
        repository.getAlgorithmsByCategory(category)
}