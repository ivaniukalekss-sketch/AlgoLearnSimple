package com.ivaniuk.algolearnsimple.domain.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DijkstraVisualizer : AlgorithmVisualizer {
    override val algorithmId: Int = 5
    override val algorithmName: String = "Dijkstra's Algorithm"
    override val algorithmType: AlgorithmType = AlgorithmType.GRAPH_TRAVERSAL

    override fun visualize(input: Any): Flow<List<VisualizationStep>> = flow {
        val data = input as? Triple<Map<Int, List<Pair<Int, Int>>>, Int, Int>
            ?: getDefaultInput() as Triple<Map<Int, List<Pair<Int, Int>>>, Int, Int>

        val graph = data.first
        val startNode = data.second
        val targetNode = data.third

        val steps = mutableListOf<VisualizationStep>()
        var stepCounter = 0

        // Шаг 0: Инициализация
        val distances = mutableMapOf<Int, Int>()
        val visited = mutableSetOf<Int>()
        val previous = mutableMapOf<Int, Int>()
        val unvisited = graph.keys.toMutableSet()

        // Инициализируем расстояния
        graph.keys.forEach { vertex ->
            distances[vertex] = if (vertex == startNode) 0 else Int.MAX_VALUE
            previous[vertex] = -1
        }

        // Шаг 1: Начало алгоритма
        steps.add(
            createStep(
                stepNumber = stepCounter++,
                graph = graph,
                distances = distances,
                visited = visited,
                currentNode = null,
                startNode = startNode,
                targetNode = targetNode,
                description = "🚀 Начинаем алгоритм Дейкстры\n" +
                        "• Стартовая вершина: $startNode\n" +
                        "• Целевая вершина: $targetNode\n" +
                        "• Всего вершин: ${graph.size}",
                highlightedEdges = emptySet()
            )
        )

        // Шаг 2: Показываем начальные расстояния
        steps.add(
            createStep(
                stepNumber = stepCounter++,
                graph = graph,
                distances = distances,
                visited = visited,
                currentNode = startNode,
                startNode = startNode,
                targetNode = targetNode,
                description = "📊 Инициализация расстояний:\n" +
                        "• distance[$startNode] = 0 (стартовая вершина)\n" +
                        "• distance[остальные] = ∞",
                highlightedEdges = emptySet()
            )
        )

        // Основной цикл алгоритма Дейкстры
        while (unvisited.isNotEmpty()) {
            // Находим вершину с минимальным расстоянием
            val current = unvisited.minByOrNull { distances[it]!! }!!

            if (distances[current] == Int.MAX_VALUE) {
                // Нет достижимых вершин
                steps.add(
                    createStep(
                        stepNumber = stepCounter++,
                        graph = graph,
                        distances = distances,
                        visited = visited,
                        currentNode = null,
                        startNode = startNode,
                        targetNode = targetNode,
                        description = "⚠️ Больше нет достижимых вершин",
                        highlightedEdges = emptySet()
                    )
                )
                break
            }

            // Помечаем как посещённую
            unvisited.remove(current)
            visited.add(current)

            // Шаг: Выбор текущей вершины
            steps.add(
                createStep(
                    stepNumber = stepCounter++,
                    graph = graph,
                    distances = distances,
                    visited = visited,
                    currentNode = current,
                    startNode = startNode,
                    targetNode = targetNode,
                    description = "🎯 Выбираем вершину с минимальным расстоянием: $current\n" +
                            "• Расстояние до $current = ${distances[current]}\n" +
                            "• Помечаем как посещённую",
                    highlightedEdges = emptySet()
                )
            )

            // Если достигли целевой вершины
            if (current == targetNode) {
                val path = reconstructPath(previous, startNode, targetNode)

                steps.add(
                    createStep(
                        stepNumber = stepCounter++,
                        graph = graph,
                        distances = distances,
                        visited = visited,
                        currentNode = current,
                        startNode = startNode,
                        targetNode = targetNode,
                        path = path,
                        description = "✅ Найден кратчайший путь до $targetNode!\n" +
                                "• Путь: ${path.joinToString(" → ")}\n" +
                                "• Длина: ${distances[targetNode]}",
                        highlightedEdges = getPathEdges(path)
                    )
                )
            }

            // Обновляем расстояния до соседей
            val neighbors = graph[current] ?: emptyList()

            if (neighbors.isNotEmpty()) {
                steps.add(
                    createStep(
                        stepNumber = stepCounter++,
                        graph = graph,
                        distances = distances,
                        visited = visited,
                        currentNode = current,
                        startNode = startNode,
                        targetNode = targetNode,
                        description = "🔍 Рассматриваем соседей вершины $current:\n" +
                                neighbors.joinToString("\n") {
                                    "• $current → ${it.first} (вес = ${it.second})"
                                },
                        highlightedEdges = neighbors.map { Pair(current, it.first) }.toSet()
                    )
                )

                neighbors.forEach { (neighbor, weight) ->
                    val newDistance = distances[current]!! + weight
                    val oldDistance = distances[neighbor]!!

                    val edge = Pair(current, neighbor)

                    if (newDistance < oldDistance) {
                        distances[neighbor] = newDistance
                        previous[neighbor] = current

                        steps.add(
                            createStep(
                                stepNumber = stepCounter++,
                                graph = graph,
                                distances = distances,
                                visited = visited,
                                currentNode = current,
                                comparingNodes = setOf(neighbor),
                                startNode = startNode,
                                targetNode = targetNode,
                                description = "📈 Обновляем расстояние до $neighbor:\n" +
                                        "• Старое: ${if (oldDistance == Int.MAX_VALUE) "∞" else oldDistance}\n" +
                                        "• Новое: $newDistance (через $current)\n" +
                                        "• Разница: +$weight",
                                highlightedEdges = setOf(edge)
                            )
                        )
                    } else {
                        steps.add(
                            createStep(
                                stepNumber = stepCounter++,
                                graph = graph,
                                distances = distances,
                                visited = visited,
                                currentNode = current,
                                comparingNodes = setOf(neighbor),
                                startNode = startNode,
                                targetNode = targetNode,
                                description = "⏭️ Пропускаем соседа $neighbor:\n" +
                                        "• Текущее расстояние: ${if (oldDistance == Int.MAX_VALUE) "∞" else oldDistance}\n" +
                                        "• Предложенное: $newDistance (не лучше)",
                                highlightedEdges = setOf(edge)
                            )
                        )
                    }
                }
            }

            // Шаг: Текущее состояние
            val currentDistances = visited.sorted().joinToString("\n") {
                "• $it: ${distances[it]}"
            }

            steps.add(
                createStep(
                    stepNumber = stepCounter++,
                    graph = graph,
                    distances = distances,
                    visited = visited,
                    currentNode = null,
                    startNode = startNode,
                    targetNode = targetNode,
                    description = "📊 Текущее состояние:\n" +
                            "Посещённые вершины (${visited.size}/${graph.size}):\n$currentDistances",
                    highlightedEdges = emptySet()
                )
            )
        }

        // Финальный шаг
        val finalDistances = graph.keys.sorted().joinToString("\n") {
            "• $it: ${if (distances[it] == Int.MAX_VALUE) "∞" else distances[it]}"
        }

        val path = if (distances[targetNode] != Int.MAX_VALUE) {
            val p = reconstructPath(previous, startNode, targetNode)
            "🎯 Кратчайший путь: ${p.joinToString(" → ")}\n" +
                    "📏 Длина пути: ${distances[targetNode]}"
        } else {
            "❌ Целевая вершина $targetNode недостижима"
        }

        steps.add(
            createStep(
                stepNumber = stepCounter,
                graph = graph,
                distances = distances,
                visited = visited,
                currentNode = null,
                startNode = startNode,
                targetNode = targetNode,
                path = if (distances[targetNode] != Int.MAX_VALUE)
                    reconstructPath(previous, startNode, targetNode) else emptyList(),
                description = "🏁 Алгоритм завершён!\n\n" +
                        "📊 Итоговые расстояния от $startNode:\n$finalDistances\n\n" +
                        path,
                highlightedEdges = if (distances[targetNode] != Int.MAX_VALUE)
                    getPathEdges(reconstructPath(previous, startNode, targetNode)) else emptySet()
            )
        )

        emit(steps)
    }

    // Вспомогательные функции
    private fun createStep(
        stepNumber: Int,
        graph: Map<Int, List<Pair<Int, Int>>>,
        distances: Map<Int, Int>,
        visited: Set<Int>,
        currentNode: Int?,
        startNode: Int,
        targetNode: Int,
        comparingNodes: Set<Int> = emptySet(),
        path: List<Int> = emptyList(),
        description: String,
        highlightedEdges: Set<Pair<Int, Int>>
    ): VisualizationStep {
        // Конвертируем взвешенный граф в простой для отображения
        val simpleGraph = convertToSimpleGraph(graph)

        // Создаём карту весов рёбер
        val edgeWeights = getEdgeWeights(graph)

        // Форматируем расстояния для отображения
        val formattedDistances = distances.mapValues {
            if (it.value == Int.MAX_VALUE) "∞" else it.value.toString()
        }

        // Создаем customData с правильными типами
        val customData = mutableMapOf<String, Any>(
            "edgeWeights" to edgeWeights,
            "nodeDistances" to formattedDistances,
            "startNode" to startNode,
            "targetNode" to targetNode,
            "visitedNodes" to visited,
            "pathNodes" to path.toSet(),
            "comparingNodes" to comparingNodes,
            "highlightedEdges" to highlightedEdges
        )

        // Добавляем currentNode только если он не null
        currentNode?.let {
            customData["currentNode"] = it
        }

        return VisualizationStep(
            stepNumber = stepNumber,
            graph = simpleGraph,
            customData = customData,
            description = description,
            codeLine = when {
                stepNumber == 0 -> "// Инициализация алгоритма Дейкстры"
                currentNode != null && description.contains("Выбираем") ->
                    "val current = unvisited.minBy { distances[it]!! }"
                description.contains("Обновляем") ->
                    "if (newDistance < distances[neighbor]!!) distances[neighbor] = newDistance"
                else -> "// Основной цикл алгоритма"
            }
        )
    }

    private fun convertToSimpleGraph(weightedGraph: Map<Int, List<Pair<Int, Int>>>): Map<Int, List<Int>> {
        return weightedGraph.mapValues { (_, neighbors) ->
            neighbors.map { it.first }
        }
    }

    private fun getEdgeWeights(weightedGraph: Map<Int, List<Pair<Int, Int>>>): Map<Pair<Int, Int>, Int> {
        val weights = mutableMapOf<Pair<Int, Int>, Int>()
        weightedGraph.forEach { (nodeId, neighbors) ->
            neighbors.forEach { (neighborId, weight) ->
                weights[Pair(nodeId, neighborId)] = weight
            }
        }
        return weights
    }

    private fun reconstructPath(previous: Map<Int, Int>, start: Int, target: Int): List<Int> {
        val path = mutableListOf<Int>()
        var current = target

        while (current != -1 && current != start) {
            path.add(current)
            current = previous[current] ?: -1
        }
        path.add(start)
        return path.reversed()
    }

    private fun getPathEdges(path: List<Int>): Set<Pair<Int, Int>> {
        return path.zipWithNext().toSet()
    }

    override fun getDefaultInput(): Any {
        // Пример графа для демонстрации
        val graph = mapOf(
            0 to listOf(1 to 4, 2 to 1),
            1 to listOf(3 to 1),
            2 to listOf(1 to 2, 3 to 5),
            3 to listOf()
        )
        return Triple(graph, 0, 3) // Старт: 0, Цель: 3
    }

    override fun getInputDescription(): String {
        return "Взвешенный граф (список смежности), стартовая и целевая вершины\n" +
                "Формат: Map<Int, List<Pair<Int, Int>>>\n" +
                "Пример: 0 -> [(1, 4), (2, 1)]"
    }
}