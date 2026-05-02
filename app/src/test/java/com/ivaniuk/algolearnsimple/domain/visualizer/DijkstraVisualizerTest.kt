package com.ivaniuk.algolearnsimple.domain.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class DijkstraVisualizerTest {

    private val visualizer = DijkstraVisualizer()

    @Test
    fun testAlgorithmProperties() {
        assertEquals(7, visualizer.algorithmId)
        assertEquals("Dijkstra", visualizer.algorithmName)
        assertEquals(AlgorithmType.GRAPH_PATHFINDING, visualizer.algorithmType)
    }

    @Test
    fun testGetDefaultInputReturnsPair() {
        val input = visualizer.getDefaultInput()
        assertTrue(input is Pair<*, *>)
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