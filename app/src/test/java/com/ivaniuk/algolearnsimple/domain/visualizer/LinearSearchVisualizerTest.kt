package com.ivaniuk.algolearnsimple.domain.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class LinearSearchVisualizerTest {

    private val visualizer = LinearSearchVisualizer()

    @Test
    fun testAlgorithmProperties() {
        assertEquals(10, visualizer.algorithmId)
        assertEquals("Linear Search", visualizer.algorithmName)
        assertEquals(AlgorithmType.SEARCHING_ARRAY, visualizer.algorithmType)
    }

    @Test
    fun testGetDefaultInputReturnsPair() {
        val input = visualizer.getDefaultInput()
        assertTrue(input is Pair<*, *>)
        val pair = input as Pair<*, *>
        assertTrue(pair.first is List<*>)
        assertTrue(pair.second is Int)
    }

    @Test
    fun testGenerateRandomInputReturnsPair() = runTest {
        val randomInput = visualizer.generateRandomInput()
        assertTrue(randomInput is Pair<*, *>)
    }

    @Test
    fun testVisualizeReturnsNonEmptySteps() = runTest {
        val input = visualizer.getDefaultInput()
        val steps = visualizer.visualize(input).first()
        assertTrue(steps.isNotEmpty())
    }
}