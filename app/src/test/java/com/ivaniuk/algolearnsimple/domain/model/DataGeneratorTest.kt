package com.ivaniuk.algolearnsimple.domain.model

import org.junit.Assert.*
import org.junit.Test

class DataGeneratorTest {

    @Test
    fun `test generateRandomArray returns correct size`() {
        val array = DataGenerator.generateRandomArray(10)
        assertEquals(10, array.size)
    }

    @Test
    fun `test generateSortedArray returns sorted list`() {
        val array = DataGenerator.generateSortedArray(5)

        for (i in 0 until array.size - 1) {
            assertTrue(array[i] <= array[i + 1])
        }
    }

    @Test
    fun `test generateBinarySearchData returns pair`() {
        val data = DataGenerator.generateBinarySearchData()
        assertTrue(data is Pair<*, *>)

        val (array, target) = data
        assertTrue(array is List<*>)
        assertTrue(target is Int)
    }
    @Test
    fun `test generateRandomGraph returns connected graph`() {
        val verticesCount = 6
        val graph = DataGenerator.generateRandomGraph(verticesCount)

        assertEquals(verticesCount, graph.keys.size)

        for (i in 0 until verticesCount) {
            assertTrue("Vertex $i should exist in graph", graph.containsKey(i))
        }

        for ((vertex, neighbors) in graph) {
            assertNotNull("Vertex $vertex should have neighbors list", neighbors)
        }

        val verticesWithNeighbors = graph.values.count { it.isNotEmpty() }
        assertEquals("All vertices should have neighbors", verticesCount, verticesWithNeighbors)
    }

    @Test
    fun `test generateRandomArray returns values in range`() {
        val size = 10
        val array = DataGenerator.generateRandomArray(size)

        assertEquals(size, array.size)

        array.forEach { value ->
            assertTrue("Value $value should be between 1 and 99", value in 1..99)
        }
    }

    @Test
    fun `test generateSortedArray returns increasing sequence`() {
        val size = 8
        val array = DataGenerator.generateSortedArray(size)

        assertEquals(size, array.size)

        for (i in 0 until array.size - 1) {
            assertTrue("Array should be sorted: ${array[i]} <= ${array[i + 1]}",
                array[i] <= array[i + 1])
        }

        val firstDiff = if (array.size > 1) array[1] - array[0] else 0
        assertTrue("Array should have positive step", firstDiff > 0)
    }
}