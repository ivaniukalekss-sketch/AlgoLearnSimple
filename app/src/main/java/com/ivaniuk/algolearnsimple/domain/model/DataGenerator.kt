package com.ivaniuk.algolearnsimple.domain.util

import kotlin.random.Random

object DataGenerator {

    // Генерация случайного массива
    fun generateRandomArray(size: Int = Random.nextInt(8, 15)): List<Int> {
        return List(size) { Random.nextInt(1, 100) }
    }

    // Генерация отсортированного массива для бинарного поиска
    fun generateSortedArray(size: Int = Random.nextInt(8, 15)): List<Int> {
        val start = Random.nextInt(1, 20)
        val step = Random.nextInt(1, 5)
        return List(size) { index -> start + index * step }
    }

    // Генерация данных для бинарного поиска
    fun generateBinarySearchData(): Pair<List<Int>, Int> {
        val array = generateSortedArray()
        val target = if (Random.nextBoolean()) {
            array.random() // Существующий элемент
        } else {
            array.maxOrNull()!! + Random.nextInt(5, 15) // Несуществующий
        }
        return Pair(array, target)
    }

    // Генерация случайного графа
    fun generateRandomGraph(vertices: Int = Random.nextInt(5, 8)): Map<Int, List<Int>> {
        val graph = mutableMapOf<Int, MutableList<Int>>()

        for (i in 0 until vertices) {
            graph[i] = mutableListOf()
        }

        for (i in 0 until vertices) {
            for (j in i + 1 until vertices) {
                if (Random.nextDouble() < 0.4) { // 40% вероятность ребра
                    graph[i]?.add(j)
                    graph[j]?.add(i)
                }
            }
        }

        // Гарантируем связность
        for (i in 0 until vertices) {
            if (graph[i]?.isEmpty() == true) {
                val other = (0 until vertices).filter { it != i }.random()
                graph[i]?.add(other)
                graph[other]?.add(i)
            }
        }

        return graph
    }

    // Генерация взвешенного графа для Дейкстры
    fun generateWeightedGraph(): Triple<Map<Int, List<Pair<Int, Int>>>, Int, Int> {
        val vertices = 5
        val graph = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()

        for (i in 0 until vertices) {
            graph[i] = mutableListOf()
        }

        // Простой граф с гарантированными путями
        graph[0]?.addAll(listOf(1 to 4, 2 to 1))
        graph[1]?.addAll(listOf(0 to 4, 3 to 1))
        graph[2]?.addAll(listOf(0 to 1, 1 to 2, 3 to 5))
        graph[3]?.addAll(listOf(1 to 1, 2 to 5))

        return Triple(graph, 0, 3)
    }
}