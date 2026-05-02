package com.ivaniuk.algolearnsimple.domain.model

import kotlin.random.Random

object DataGenerator {

    fun generateRandomArray(size: Int = Random.nextInt(8, 15)): List<Int> {
        return List(size) { Random.nextInt(1, 100) }
    }

    fun generateSortedArray(size: Int = Random.nextInt(8, 15)): List<Int> {
        val start = Random.nextInt(1, 20)
        val step = Random.nextInt(1, 5)
        return List(size) { index -> start + index * step }
    }

    fun generateBinarySearchData(): Pair<List<Int>, Int> {
        val array = generateSortedArray()
        val target = if (Random.nextBoolean()) {
            array.random()
        } else {
            array.maxOrNull()!! + Random.nextInt(5, 15)
        }
        return Pair(array, target)
    }

    fun generateRandomGraph(vertices: Int = Random.nextInt(5, 8)): Map<Int, List<Int>> {
        val graph = mutableMapOf<Int, MutableList<Int>>()

        for (i in 0 until vertices) {
            graph[i] = mutableListOf()
        }

        for (i in 0 until vertices) {
            for (j in i + 1 until vertices) {
                if (Random.nextDouble() < 0.4) {
                    graph[i]?.add(j)
                    graph[j]?.add(i)
                }
            }
        }

        for (i in 0 until vertices) {
            if (graph[i]?.isEmpty() == true) {
                val other = (0 until vertices).filter { it != i }.random()
                graph[i]?.add(other)
                graph[other]?.add(i)
            }
        }

        return graph
    }

    fun generateRandomWeightedGraph(vertices: Int = 5): Map<Int, List<Pair<Int, Int>>> {
        val graph = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()

        for (i in 0 until vertices) {
            graph[i] = mutableListOf()
        }

        for (i in 0 until vertices) {
            for (j in i + 1 until vertices) {
                if (Random.nextDouble() < 0.4) {
                    val weight = Random.nextInt(1, 15)
                    graph[i]?.add(Pair(j, weight))
                    graph[j]?.add(Pair(i, weight))
                }
            }
        }

        for (i in 0 until vertices) {
            if (graph[i]?.isEmpty() == true) {
                val other = (0 until vertices).filter { it != i }.random()
                val weight = Random.nextInt(1, 15)
                graph[i]?.add(Pair(other, weight))
                graph[other]?.add(Pair(i, weight))
            }
        }

        return graph
    }

}