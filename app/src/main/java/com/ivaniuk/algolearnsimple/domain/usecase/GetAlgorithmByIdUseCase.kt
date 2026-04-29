package com.ivaniuk.algolearnsimple.domain.usecase

import com.ivaniuk.algolearnsimple.domain.model.Algorithm
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import javax.inject.Inject

class GetAlgorithmByIdUseCase @Inject constructor(
    private val repository: AlgorithmRepository
) {
    suspend operator fun invoke(id: Int): Algorithm? = repository.getAlgorithmById(id)
}