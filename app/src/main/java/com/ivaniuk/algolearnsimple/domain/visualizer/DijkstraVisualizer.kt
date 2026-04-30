package com.ivaniuk.algolearnsimple.domain.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class DijkstraVisualizer : AlgorithmVisualizer {
    override val algorithmId: Int = 7
    override val algorithmName: String = "Dijkstra"
    override val algorithmType: AlgorithmType = AlgorithmType.GRAPH_PATHFINDING

    override fun generateRandomInput(): Any {
        val numVertices = (5..8).random()
        val graph = generateRandomWeightedGraph(numVertices)
        val startNode = (0 until numVertices).random()
        return Pair(graph, startNode)
    }

    private fun generateRandomWeightedGraph(vertices: Int): Map<Int, List<Int>> {
        val graph = mutableMapOf<Int, MutableList<Int>>()

        for (i in 0 until vertices) {
            graph[i] = mutableListOf()
        }

        for (i in 0 until vertices) {
            for (j in i + 1 until vertices) {
                if (Random.nextDouble() < 0.35) {
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

    override fun visualize(input: Any): Flow<List<VisualizationStep>> = flow {
        val data = when {
            input is Pair<*, *> && input.first is Map<*, *> && input.second is Int -> {
                val graph = convertToGraph(input.first as Map<*, *>)
                val start = input.second as Int
                Pair(graph, start)
            }
            else -> getDefaultInput()
        }

        val graph = data.first
        val startNode = data.second

        val steps = mutableListOf<VisualizationStep>()
        var stepCounter = 0

        val distances = mutableMapOf<Int, Int>().apply {
            graph.keys.forEach { node ->
                this[node] = if (node == startNode) 0 else Int.MAX_VALUE
            }
        }
        val visited = mutableSetOf<Int>()
        val unvisited = graph.keys.toMutableSet()

        steps.add(
            VisualizationStep(
                stepNumber = stepCounter++,
                graph = graph,
                description = "Начинаем алгоритм Дейкстры из вершины $startNode",
                codeLine = "distances[$startNode] = 0, остальные = ∞"
            )
        )

        steps.add(
            VisualizationStep(
                stepNumber = stepCounter++,
                graph = graph,
                highlightedIndices = setOf(startNode),
                description = "Начальное расстояние до стартовой вершины = 0"
            )
        )

        while (unvisited.isNotEmpty()) {
            val current = unvisited.minByOrNull { distances[it] ?: Int.MAX_VALUE } ?: break
            if (distances[current] == Int.MAX_VALUE) break

            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    graph = graph,
                    currentIndex = current,
                    highlightedIndices = visited,
                    description = "Выбираем вершину с минимальным расстоянием: $current (расстояние = ${distances[current]})"
                )
            )

            visited.add(current)
            unvisited.remove(current)

            val neighbors = graph[current] ?: emptyList()

            if (neighbors.isNotEmpty()) {
                steps.add(
                    VisualizationStep(
                        stepNumber = stepCounter++,
                        graph = graph,
                        currentIndex = current,
                        comparingIndices = neighbors.toSet(),
                        highlightedIndices = visited,
                        description = "Рассматриваем соседей вершины $current: ${neighbors.joinToString()}"
                    )
                )
            }

            for (neighbor in neighbors) {
                if (neighbor in visited) continue

                val edgeWeight = getEdgeWeight(current, neighbor)
                val newDistance = (distances[current] ?: Int.MAX_VALUE) + edgeWeight
                val oldDistance = distances[neighbor] ?: Int.MAX_VALUE

                steps.add(
                    VisualizationStep(
                        stepNumber = stepCounter++,
                        graph = graph,
                        currentIndex = current,
                        comparingIndices = setOf(neighbor),
                        highlightedIndices = visited,
                        description = "Путь $current → $neighbor: ${distances[current]} + $edgeWeight = $newDistance (было: ${if (oldDistance == Int.MAX_VALUE) "∞" else oldDistance})"
                    )
                )

                if (newDistance < oldDistance) {
                    distances[neighbor] = newDistance

                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            graph = graph,
                            currentIndex = current,
                            comparingIndices = setOf(neighbor),
                            sortedIndices = setOf(neighbor),
                            highlightedIndices = visited,
                            description = "✅ Найден более короткий путь к $neighbor: $newDistance",
                            codeLine = "distances[$neighbor] = $newDistance"
                        )
                    )
                }
            }

            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    graph = graph,
                    sortedIndices = visited,
                    description = "Посещённые вершины: ${visited.joinToString()}. Осталось: ${unvisited.joinToString()}"
                )
            )
        }

        steps.add(
            VisualizationStep(
                stepNumber = stepCounter++,
                graph = graph,
                sortedIndices = visited,
                description = "Алгоритм Дейкстры завершён! Расстояния от вершины $startNode: ${distances.map { (k, v) -> "$k:${if (v == Int.MAX_VALUE) "∞" else v}" }.joinToString()}"
            )
        )

        emit(steps)
    }

    override fun getDefaultInput(): Pair<Map<Int, List<Int>>, Int> {
        val graph = mapOf(
            0 to listOf(1, 2),
            1 to listOf(0, 2, 3),
            2 to listOf(0, 1, 3, 4),
            3 to listOf(1, 2, 4),
            4 to listOf(2, 3)
        )
        return Pair(graph, 0)
    }

    override fun getInputDescription(): String {
        return "Граф с весами рёбер (0-4) и стартовая вершина. Веса: 0-1:4, 0-2:2, 1-2:1, 1-3:5, 2-3:8, 2-4:10, 3-4:2"
    }

    private fun convertToGraph(input: Map<*, *>): Map<Int, List<Int>> {
        return input.mapNotNull { (key, value) ->
            val nodeId = when (key) {
                is Int -> key
                is Number -> key.toInt()
                else -> return@mapNotNull null
            }

            val neighbors = when (value) {
                is List<*> -> value.filterIsInstance<Int>()
                else -> emptyList()
            }

            nodeId to neighbors
        }.toMap()
    }

    private fun getEdgeWeight(current: Int, neighbor: Int): Int {
        val weights = mapOf(
            Pair(0, 1) to 4, Pair(1, 0) to 4,
            Pair(0, 2) to 2, Pair(2, 0) to 2,
            Pair(1, 2) to 1, Pair(2, 1) to 1,
            Pair(1, 3) to 5, Pair(3, 1) to 5,
            Pair(2, 3) to 8, Pair(3, 2) to 8,
            Pair(2, 4) to 10, Pair(4, 2) to 10,
            Pair(3, 4) to 2, Pair(4, 3) to 2
        )
        return weights[Pair(current, neighbor)] ?: 1
    }
}