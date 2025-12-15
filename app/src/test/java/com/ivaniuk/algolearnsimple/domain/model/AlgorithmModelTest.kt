package com.ivaniuk.algolearnsimple.domain.model

import org.junit.Assert.*
import org.junit.Test

class AlgorithmModelTest {

    @Test
    fun `test Algorithm data class equals and hashcode`() {
        val algorithm1 = Algorithm(
            id = 1,
            title = "Test Algorithm",
            description = "Test Description",
            category = AlgorithmCategory.SORTING,
            complexity = "O(1)",
            codeExample = "test code",
            steps = listOf("Step 1", "Step 2"),
            isFavorite = false
        )

        val algorithm2 = Algorithm(
            id = 1,
            title = "Test Algorithm",
            description = "Test Description",
            category = AlgorithmCategory.SORTING,
            complexity = "O(1)",
            codeExample = "test code",
            steps = listOf("Step 1", "Step 2"),
            isFavorite = false
        )

        assertEquals(algorithm1, algorithm2)
        assertEquals(algorithm1.hashCode(), algorithm2.hashCode())
    }

    @Test
    fun `test Algorithm data class not equals`() {
        val algorithm1 = Algorithm(
            id = 1,
            title = "Algorithm 1",
            description = "Description 1",
            category = AlgorithmCategory.SORTING,
            complexity = "O(1)",
            codeExample = "code 1",
            isFavorite = false
        )

        val algorithm2 = Algorithm(
            id = 2,
            title = "Algorithm 2",
            description = "Description 2",
            category = AlgorithmCategory.SEARCHING,
            complexity = "O(log n)",
            codeExample = "code 2",
            isFavorite = true
        )

        assertNotEquals(algorithm1, algorithm2)
    }

    @Test
    fun `test AlgorithmCategory enum values`() {
        val values = AlgorithmCategory.values()
        assertEquals(3, values.size)
        assertTrue(values.contains(AlgorithmCategory.SORTING))
        assertTrue(values.contains(AlgorithmCategory.SEARCHING))
        assertTrue(values.contains(AlgorithmCategory.GRAPH))
    }

    @Test
    fun `test VisualizationStep equals and hashcode`() {
        val step1 = VisualizationStep(
            stepNumber = 1,
            array = listOf(1, 2, 3),
            description = "Test step"
        )

        val step2 = VisualizationStep(
            stepNumber = 1,
            array = listOf(1, 2, 3),
            description = "Test step"
        )

        assertEquals(step1, step2)
        assertEquals(step1.hashCode(), step2.hashCode())
    }
}