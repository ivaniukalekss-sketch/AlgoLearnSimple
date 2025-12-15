package com.ivaniuk.algolearnsimple.domain.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class BubbleSortVisualizerTest {

    @Test
    fun `test getDefaultInput returns valid array`() {
        val visualizer = BubbleSortVisualizer()
        val input = visualizer.getDefaultInput()

        assertTrue(input is List<*>)
        assertTrue((input as List<*>).isNotEmpty())
    }

    @Test
    fun `test algorithm properties`() {
        val visualizer = BubbleSortVisualizer()

        assertEquals(1, visualizer.algorithmId)
        assertEquals("Bubble Sort", visualizer.algorithmName)
        assertEquals(AlgorithmType.SORTING_ARRAY, visualizer.algorithmType)
    }

    @Test
    fun `test generateRandomInput returns list`() = runTest {
        val visualizer = BubbleSortVisualizer()
        val randomInput = visualizer.generateRandomInput()

        assertTrue(randomInput is List<*>)
        val list = randomInput as List<*>
        assertTrue(list.size in 8..15)
    }
    @Test
    fun `test visualize returns non-empty steps`() = runTest {
        val visualizer = BubbleSortVisualizer()
        val input = visualizer.getDefaultInput()
        val steps = visualizer.visualize(input).first()
        assertTrue(steps.isNotEmpty())
    }
}