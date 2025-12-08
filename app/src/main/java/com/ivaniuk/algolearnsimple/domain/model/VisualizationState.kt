package com.ivaniuk.algolearnsimple.domain.model

data class VisualizationState(
    val algorithmId: Int,
    val algorithmName: String,
    val array: List<Int>,
    val currentStep: Int = 0,
    val totalSteps: Int = 0,
    val isPlaying: Boolean = false,
    val speed: Speed = Speed.MEDIUM,
    val highlightedIndices: Set<Int> = emptySet(),
    val swappedIndices: Set<Int> = emptySet(),
    val sortedIndices: Set<Int> = emptySet(),
    val description: String = ""
)

enum class Speed(val delayMs: Long) {
    SLOW(1000L),
    MEDIUM(500L),
    FAST(200L),
    VERY_FAST(100L)
}