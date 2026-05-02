package com.ivaniuk.algolearnsimple.domain.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class MergeSortVisualizerTest {

    private val visualizer = MergeSortVisualizer()

    @Test
    fun testAlgorithmProperties() {
        assertEquals(6, visualizer.algorithmId)
        assertEquals("Merge Sort", visualizer.algorithmName)
        assertEquals(AlgorithmType.SORTING_ARRAY, visualizer.algorithmType)
    }

    @Test
    fun testGetDefaultInputReturnsNonEmptyList() {
        val input = visualizer.getDefaultInput()
        assertTrue(input is List<*>)
        assertTrue((input as List<*>).isNotEmpty())
    }

    @Test
    fun testGenerateRandomInputReturnsList() = runTest {
        val randomInput = visualizer.generateRandomInput()
        assertTrue(randomInput is List<*>)
        val list = randomInput as List<*>
        assertTrue(list.size in 8..15)
    }

    @Test
    fun testVisualizeReturnsNonEmptySteps() = runTest {
        val input = visualizer.getDefaultInput()
        val steps = visualizer.visualize(input).first()
        assertTrue(steps.isNotEmpty())
    }
}