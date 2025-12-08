package com.ivaniuk.algolearnsimple.domain.model

data class VisualizationStep(
    val stepNumber: Int,
    val array: List<Int>? = null,
    val graph: Map<Int, List<Int>>? = null,
    val highlightedIndices: Set<Int> = emptySet(),
    val comparingIndices: Set<Int> = emptySet(),
    val swappedIndices: Set<Int> = emptySet(),
    val sortedIndices: Set<Int> = emptySet(),
    val currentIndex: Int? = null,
    val targetIndex: Int? = null,
    val description: String,
    val codeLine: String? = null
)