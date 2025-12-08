package com.ivaniuk.algolearnsimple.data.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BinarySearchVisualizer : AlgorithmVisualizer {
    override val algorithmId: Int = 2
    override val algorithmName: String = "Binary Search"
    override val algorithmType: AlgorithmType = AlgorithmType.SEARCHING_ARRAY

    override fun visualize(input: Any): Flow<List<VisualizationStep>> = flow {
        val data = input as? Pair<List<Int>, Int> ?: getDefaultInput() as Pair<List<Int>, Int>
        val array = data.first
        val target = data.second

        val steps = mutableListOf<VisualizationStep>()
        var stepCounter = 0

        steps.add(
            VisualizationStep(
                stepNumber = stepCounter++,
                array = array,
                description = "Начинаем бинарный поиск элемента $target в отсортированном массиве"
            )
        )

        var left = 0
        var right = array.size - 1

        while (left <= right) {
            val mid = left + (right - left) / 2

            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    array = array,
                    highlightedIndices = (left..right).toSet(),
                    comparingIndices = setOf(mid),
                    description = "Диапазон поиска: [$left, $right]. Середина: индекс $mid (значение ${array[mid]})",
                    codeLine = "mid = left + (right - left) / 2  // $mid"
                )
            )

            when {
                array[mid] == target -> {
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            array = array,
                            highlightedIndices = setOf(mid),
                            description = "✅ Элемент $target найден на позиции $mid!",
                            codeLine = "return $mid  // элемент найден"
                        )
                    )
                    emit(steps)
                    return@flow
                }

                array[mid] < target -> {
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            array = array,
                            highlightedIndices = (mid + 1..right).toSet(),
                            description = "${array[mid]} < $target → ищем в правой половине [${mid + 1}, $right]",
                            codeLine = "left = mid + 1  // ищем справа"
                        )
                    )
                    left = mid + 1
                }

                else -> {
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            array = array,
                            highlightedIndices = (left until mid).toSet(),
                            description = "${array[mid]} > $target → ищем в левой половине [$left, ${mid - 1}]",
                            codeLine = "right = mid - 1  // ищем слева"
                        )
                    )
                    right = mid - 1
                }
            }
        }

        steps.add(
            VisualizationStep(
                stepNumber = stepCounter,
                array = array,
                description = "❌ Элемент $target не найден в массиве",
                codeLine = "return -1  // элемент не найден"
            )
        )

        emit(steps)
    }

    override fun getDefaultInput(): Any {
        return Pair(
            listOf(2, 5, 8, 12, 16, 23, 38, 45, 56, 67, 78, 89, 90),
            45
        )
    }

    override fun getInputDescription(): String {
        return "Отсортированный массив целых чисел и элемент для поиска"
    }
}