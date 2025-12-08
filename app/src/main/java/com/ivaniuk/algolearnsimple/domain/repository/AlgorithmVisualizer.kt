package com.ivaniuk.algolearnsimple.domain.repository

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import kotlinx.coroutines.flow.Flow

interface AlgorithmVisualizer {
    val algorithmId: Int
    val algorithmName: String
    val algorithmType: AlgorithmType

    fun visualize(input: Any): Flow<List<VisualizationStep>>
    fun getDefaultInput(): Any
    fun getInputDescription(): String
}