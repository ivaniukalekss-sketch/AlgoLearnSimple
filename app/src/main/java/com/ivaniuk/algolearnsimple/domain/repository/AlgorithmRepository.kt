package com.ivaniuk.algolearnsimple.domain.repository

import com.ivaniuk.algolearnsimple.domain.model.Algorithm
import kotlinx.coroutines.flow.Flow

interface AlgorithmRepository {
    fun getAllAlgorithms(): Flow<List<Algorithm>>
    fun getAlgorithmsByCategory(category: String): Flow<List<Algorithm>>
    fun getFavoriteAlgorithms(): Flow<List<Algorithm>>
    suspend fun toggleFavorite(algorithmId: Int)
    suspend fun getAlgorithmById(id: Int): Algorithm?
}