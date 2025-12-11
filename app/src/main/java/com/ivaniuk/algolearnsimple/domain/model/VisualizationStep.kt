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
    val codeLine: String? = null,
    val customData: Map<String, Any>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VisualizationStep

        if (stepNumber != other.stepNumber) return false
        if (array != other.array) return false
        if (graph != other.graph) return false
        if (description != other.description) return false
        if (codeLine != other.codeLine) return false
        if (currentIndex != other.currentIndex) return false
        if (targetIndex != other.targetIndex) return false
        if (highlightedIndices != other.highlightedIndices) return false
        if (comparingIndices != other.comparingIndices) return false
        if (swappedIndices != other.swappedIndices) return false
        if (sortedIndices != other.sortedIndices) return false
        if (customData != other.customData) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stepNumber
        result = 31 * result + (array?.hashCode() ?: 0)
        result = 31 * result + (graph?.hashCode() ?: 0)
        result = 31 * result + description.hashCode()
        result = 31 * result + (codeLine?.hashCode() ?: 0)
        result = 31 * result + (currentIndex ?: 0)
        result = 31 * result + (targetIndex ?: 0)
        result = 31 * result + highlightedIndices.hashCode()
        result = 31 * result + comparingIndices.hashCode()
        result = 31 * result + swappedIndices.hashCode()
        result = 31 * result + sortedIndices.hashCode()
        result = 31 * result + (customData?.hashCode() ?: 0)
        return result
    }
}