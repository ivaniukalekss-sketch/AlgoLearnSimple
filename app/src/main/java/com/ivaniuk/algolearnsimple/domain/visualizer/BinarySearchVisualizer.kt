package com.ivaniuk.algolearnsimple.domain.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import com.ivaniuk.algolearnsimple.domain.model.DataGenerator
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BinarySearchVisualizer : AlgorithmVisualizer {
    override val algorithmId: Int = 2
    override val algorithmName: String = "Binary Search"
    override val algorithmType: AlgorithmType = AlgorithmType.SEARCHING_ARRAY

    override fun generateRandomInput(): Any {
        return DataGenerator.generateBinarySearchData()
    }

    override fun visualize(input: Any): Flow<List<VisualizationStep>> = flow {
        val data = when {
            input is Pair<*, *> && input.first is List<*> && input.second is Int -> {
                val list = (input.first as List<*>).filterIsInstance<Int>()
                val targetValue = input.second as Int
                Pair(list, targetValue)
            }
            else -> getDefaultInput()
        }

        val array = data.first
        val target = data.second

        val steps = mutableListOf<VisualizationStep>()
        var stepCounter = 0

        steps.add(
            VisualizationStep(
                stepNumber = stepCounter++,
                array = array,
                description = "Начинаем бинарный поиск элемента $target в отсортированном массиве из ${array.size} элементов"
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
                    description = "Диапазон поиска: индексы [$left, $right]. Вычисляем середину..."
                )
            )

            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    array = array,
                    highlightedIndices = (left..right).toSet(),
                    comparingIndices = setOf(mid),
                    description = "Середина: индекс $mid (значение ${array[mid]})",
                    codeLine = "val mid = left + (right - left) / 2  // mid = $mid"
                )
            )

            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    array = array,
                    comparingIndices = setOf(mid),
                    targetIndex = array.indexOf(target).takeIf { it != -1 },
                    description = "Сравниваем ${array[mid]} с искомым значением $target"
                )
            )

            when {
                array[mid] == target -> {
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            array = array,
                            highlightedIndices = setOf(mid),
                            description = " ${array[mid]} == $target → Элемент найден!",
                            codeLine = "if (arr[mid] == target) return mid"
                        )
                    )

                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter,
                            array = array,
                            highlightedIndices = setOf(mid),
                            description = "Элемент $target найден на позиции $mid!",
                            codeLine = "return $mid  // успешный поиск"
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
                            comparingIndices = setOf(mid),
                            highlightedIndices = (mid + 1..right).toSet(),
                            description = "${array[mid]} < $target → ищем в правой половине [${mid + 1}, $right]",
                            codeLine = "left = mid + 1  // ищем справа"
                        )
                    )

                    val oldLeft = left
                    left = mid + 1
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            array = array,
                            highlightedIndices = (left..right).toSet(),
                            description = "Новый диапазон: [$left, $right] (было [$oldLeft, $right])"
                        )
                    )
                }

                else -> {
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            array = array,
                            comparingIndices = setOf(mid),
                            highlightedIndices = (left until mid).toSet(),
                            description = "${array[mid]} > $target → ищем в левой половине [$left, ${mid - 1}]",
                            codeLine = "right = mid - 1  // ищем слева"
                        )
                    )

                    val oldRight = right
                    right = mid - 1
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            array = array,
                            highlightedIndices = (left..right).toSet(),
                            description = "Новый диапазон: [$left, $right] (было [$left, $oldRight])"
                        )
                    )
                }
            }
        }

        steps.add(
            VisualizationStep(
                stepNumber = stepCounter++,
                array = array,
                description = " Элемент $target не найден в массиве",
                codeLine = "return -1  // элемент не найден"
            )
        )

        emit(steps)
    }

    override fun getDefaultInput(): Pair<List<Int>, Int> {
        return Pair(
            listOf(1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25),
            13
        )
    }

    override fun getInputDescription(): String {
        return "Отсортированный массив целых чисел и элемент для поиска"
    }
}