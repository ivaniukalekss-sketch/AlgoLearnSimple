package com.ivaniuk.algolearnsimple.data.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DFSVisualizer : AlgorithmVisualizer {
    override val algorithmId: Int = 4
    override val algorithmName: String = "DFS (Depth-First Search)"
    override val algorithmType: AlgorithmType = AlgorithmType.GRAPH_TRAVERSAL

    override fun visualize(input: Any): Flow<List<VisualizationStep>> = flow {
        val data = input as? Pair<Map<Int, List<Int>>, Int> ?: getDefaultInput() as Pair<Map<Int, List<Int>>, Int>
        val graph = data.first
        val startNode = data.second

        val steps = mutableListOf<VisualizationStep>()
        val visited = mutableSetOf<Int>()
        val stack = ArrayDeque<Int>()

        var stepCounter = 0

        // Шаг 0: Инициализация
        steps.add(
            VisualizationStep(
                stepNumber = stepCounter++,
                graph = graph,
                description = "🎯 Начинаем DFS в графе с вершины $startNode",
                codeLine = "val stack = ArrayDeque<Int>(); stack.add($startNode)"
            )
        )

        stack.addLast(startNode)

        while (stack.isNotEmpty()) {
            val current = stack.removeLast()

            // Шаг: обработка вершины
            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    graph = graph,
                    currentIndex = current,
                    highlightedIndices = visited.toSet(),
                    description = "📥 Извлекаем вершину $current из стека"
                )
            )

            if (current !in visited) {
                // Шаг: посещение вершины
                visited.add(current)
                steps.add(
                    VisualizationStep(
                        stepNumber = stepCounter++,
                        graph = graph,
                        currentIndex = current,
                        highlightedIndices = visited.toSet(),
                        sortedIndices = visited.toSet(),
                        description = "✅ Посещаем вершину $current (добавляем в visited)",
                        codeLine = "visited.add($current)"
                    )
                )

                // Получаем соседей
                val neighbors = graph[current] ?: emptyList()

                if (neighbors.isNotEmpty()) {
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            graph = graph,
                            currentIndex = current,
                            highlightedIndices = visited.toSet(),
                            comparingIndices = neighbors.toSet(),
                            description = "🔍 Находим соседей вершины $current: ${neighbors.joinToString()}"
                        )
                    )

                    // Добавляем соседей в обратном порядке (для правильного порядка обхода)
                    neighbors.reversed().forEach { neighbor ->
                        if (neighbor !in visited) {
                            steps.add(
                                VisualizationStep(
                                    stepNumber = stepCounter++,
                                    graph = graph,
                                    currentIndex = current,
                                    comparingIndices = setOf(neighbor),
                                    highlightedIndices = visited.toSet(),
                                    description = "📤 Добавляем соседа $neighbor в стек (ещё не посещён)"
                                )
                            )
                            stack.addLast(neighbor)
                        } else {
                            steps.add(
                                VisualizationStep(
                                    stepNumber = stepCounter++,
                                    graph = graph,
                                    currentIndex = current,
                                    comparingIndices = setOf(neighbor),
                                    highlightedIndices = visited.toSet(),
                                    description = "⏭️ Сосед $neighbor уже посещён - пропускаем"
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
                            description = "🔚 Вершина $current не имеет соседей (висячая вершина)"
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
                        description = "⏭️ Вершина $current уже посещена - пропускаем"
                    )
                )
            }

            // Шаг: состояние стека
            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    graph = graph,
                    highlightedIndices = visited.toSet(),
                    description = if (stack.isNotEmpty()) {
                        "📊 Текущее состояние стека (следующая вершина сверху): ${stack.joinToString()}"
                    } else {
                        "📊 Стек пуст - обход завершён"
                    }
                )
            )
        }

        // Финальный шаг
        steps.add(
            VisualizationStep(
                stepNumber = stepCounter,
                graph = graph,
                sortedIndices = visited.toSet(),
                description = "🎉 DFS завершён! Посещённые вершины в порядке обхода: ${visited.joinToString()}"
            )
        )

        emit(steps)
    }

    override fun getDefaultInput(): Any {
        val graph = mapOf(
            0 to listOf(1, 2),
            1 to listOf(0, 3, 4),
            2 to listOf(0, 5, 6),
            3 to listOf(1),
            4 to listOf(1, 7),
            5 to listOf(2),
            6 to listOf(2, 8),
            7 to listOf(4),
            8 to listOf(6)
        )
        return Pair(graph, 0)
    }

    override fun getInputDescription(): String {
        return "Граф в виде списка смежности и стартовая вершина"
    }
}