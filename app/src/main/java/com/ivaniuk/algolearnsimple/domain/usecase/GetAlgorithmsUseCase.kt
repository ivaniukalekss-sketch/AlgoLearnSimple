package com.ivaniuk.algolearnsimple.domain.usecase

import com.ivaniuk.algolearnsimple.domain.model.Algorithm
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlgorithmsUseCase @Inject constructor(
    private val repository: AlgorithmRepository
) {
    operator fun invoke(): Flow<List<Algorithm>> = repository.getAllAlgorithms()
}