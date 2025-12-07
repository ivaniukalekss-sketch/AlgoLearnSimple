package com.ivaniuk.algolearnsimple.domain.model

data class Algorithm(
    val id: Int,
    val title: String,
    val description: String,
    val category: AlgorithmCategory,
    val complexity: String,
    val codeExample: String,
    val steps: List<String> = emptyList(),
    val isFavorite: Boolean = false
)