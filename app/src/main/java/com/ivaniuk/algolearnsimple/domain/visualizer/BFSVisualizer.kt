package com.ivaniuk.algolearnsimple.domain.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import com.ivaniuk.algolearnsimple.domain.model.DataGenerator
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BFSVisualizer : AlgorithmVisualizer {
    override val algorithmId: Int = 5
    override val algorithmName: String = "BFS (Breadth-First Search)"
    override val algorithmType: AlgorithmType = AlgorithmType.GRAPH_TRAVERSAL

    override fun generateRandomInput(): Any {
        val graph = DataGenerator.generateRandomGraph()
        val startNode = 0
        return Pair(graph, startNode)
    }

    override fun visualize(input: Any): Flow<List<VisualizationStep>> = flow {
        val data = when {
            input is Pair<*, *> &&
                    input.first is Map<*, *> &&
                    input.second is Int -> {
                val map = convertToGraph(input.first as Map<*, *>)
                val start = input.second as Int
                Pair(map, start)
            }
            else -> getDefaultInput()
        }

        val graph = data.first
        val startNode = data.second

        val steps = mutableListOf<VisualizationStep>()
        val visited = mutableSetOf<Int>()
        val queue = ArrayDeque<Int>()

        var stepCounter = 0

        steps.add(
            VisualizationStep(
                stepNumber = stepCounter++,
                graph = graph,
                description = "Начинаем BFS в графе с вершины $startNode",
                codeLine = "val queue = ArrayDeque<Int>(); queue.add($startNode)"
            )
        )

        queue.addLast(startNode)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    graph = graph,
                    currentIndex = current,
                    highlightedIndices = visited.toSet(),
                    description = "Извлекаем вершину $current из начала очереди"
                )
            )

            if (current !in visited) {
                visited.add(current)
                steps.add(
                    VisualizationStep(
                        stepNumber = stepCounter++,
                        graph = graph,
                        currentIndex = current,
                        highlightedIndices = visited.toSet(),
                        sortedIndices = visited.toSet(),
                        description = "Посещаем вершину $current (добавляем в visited)",
                        codeLine = "visited.add($current)"
                    )
                )

                val neighbors = graph[current] ?: emptyList()

                if (neighbors.isNotEmpty()) {
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            graph = graph,
                            currentIndex = current,
                            highlightedIndices = visited.toSet(),
                            comparingIndices = neighbors.toSet(),
                            description = "Находим соседей вершины $current: ${neighbors.joinToString()}"
                        )
                    )

                    neighbors.forEach { neighbor ->
                        if (neighbor !in visited) {
                            steps.add(
                                VisualizationStep(
                                    stepNumber = stepCounter++,
                                    graph = graph,
                                    currentIndex = current,
                                    comparingIndices = setOf(neighbor),
                                    highlightedIndices = visited.toSet(),
                                    description = "Добавляем соседа $neighbor в конец очереди (ещё не посещён)"
                                )
                            )
                            queue.addLast(neighbor)
                        } else {
                            steps.add(
                                VisualizationStep(
                                    stepNumber = stepCounter++,
                                    graph = graph,
                                    currentIndex = current,
                                    comparingIndices = setOf(neighbor),
                                    highlightedIndices = visited.toSet(),
                                    description = "Сосед $neighbor уже посещён - пропускаем"
                                )
                            )
                        }
                    }
                } else {
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            graph = graph,
                            currentIndex = current,
                            highlightedIndices = visited.toSet(),
                            description = "Вершина $current не имеет соседей (висячая вершина)"
                        )
                    )
                }
            } else {
                steps.add(
                    VisualizationStep(
                        stepNumber = stepCounter++,
                        graph = graph,
                        currentIndex = current,
                        highlightedIndices = visited.toSet(),
                        description = "Вершина $current уже посещена - пропускаем"
                    )
                )
            }

            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    graph = graph,
                    highlightedIndices = visited.toSet(),
                    description = if (queue.isNotEmpty()) {
                        "Текущее состояние очереди (следующая вершина в начале): ${queue.joinToString()}"
                    } else {
                        "Очередь пуста - обход завершён"
                    }
                )
            )
        }

        steps.add(
            VisualizationStep(
                stepNumber = stepCounter,
                graph = graph,
                sortedIndices = visited.toSet(),
                description = "BFS завершён! Посещённые вершины в порядке обхода: ${visited.joinToString()}"
            )
        )

        emit(steps)
    }

    override fun getDefaultInput(): Pair<Map<Int, List<Int>>, Int> {
        val graph = mapOf(
            0 to listOf(1, 2, 3),
            1 to listOf(0, 4, 5),
            2 to listOf(0, 6),
            3 to listOf(0, 7),
            4 to listOf(1),
            5 to listOf(1, 8),
            6 to listOf(2),
            7 to listOf(3),
            8 to listOf(5)
        )
        return Pair(graph, 0)
    }

    override fun getInputDescription(): String {
        return "Граф в виде списка смежности и стартовая вершина"
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
                else -> emptyList<Int>()
            }

            nodeId to neighbors
        }.toMap()
    }
}