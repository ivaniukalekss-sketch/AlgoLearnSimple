package com.ivaniuk.algolearnsimple.domain.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import com.ivaniuk.algolearnsimple.domain.model.DataGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MergeSortVisualizer : AlgorithmVisualizer {
    override val algorithmId: Int = 6
    override val algorithmName: String = "Merge Sort"
    override val algorithmType: AlgorithmType = AlgorithmType.SORTING_ARRAY

    override fun generateRandomInput(): Any {
        return DataGenerator.generateRandomArray()
    }

    override fun visualize(input: Any): Flow<List<VisualizationStep>> = flow {
        val array = when (input) {
            is List<*> -> input.filterIsInstance<Int>()
            else -> getDefaultInput()
        }

        val steps = mutableListOf<VisualizationStep>()
        val mutableArray = array.toMutableList()

        steps.add(
            VisualizationStep(
                stepNumber = 0,
                array = mutableArray.toList(),
                description = "Начинаем Merge Sort (сортировка слиянием) массива: ${mutableArray.joinToString()}"
            )
        )

        suspend fun merge(left: Int, mid: Int, right: Int) {
            val leftArray = mutableArray.subList(left, mid + 1).toList()
            val rightArray = mutableArray.subList(mid + 1, right + 1).toList()

            steps.add(
                VisualizationStep(
                    stepNumber = steps.size,
                    array = mutableArray.toList(),
                    highlightedIndices = (left..right).toSet(),
                    description = "Разделяем: левая часть [${left}..${mid}] = ${leftArray.joinToString()}, правая часть [${mid + 1}..${right}] = ${rightArray.joinToString()}"
                )
            )

            var i = 0
            var j = 0
            var k = left

            while (i < leftArray.size && j < rightArray.size) {
                steps.add(
                    VisualizationStep(
                        stepNumber = steps.size,
                        array = mutableArray.toList(),
                        comparingIndices = setOf(left + i, mid + 1 + j),
                        description = "Сравниваем ${leftArray[i]} и ${rightArray[j]}"
                    )
                )

                if (leftArray[i] <= rightArray[j]) {
                    mutableArray[k] = leftArray[i]
                    steps.add(
                        VisualizationStep(
                            stepNumber = steps.size,
                            array = mutableArray.toList(),
                            swappedIndices = setOf(k),
                            description = "${leftArray[i]} ≤ ${rightArray[j]} → берём элемент из левой части"
                        )
                    )
                    i++
                } else {
                    mutableArray[k] = rightArray[j]
                    steps.add(
                        VisualizationStep(
                            stepNumber = steps.size,
                            array = mutableArray.toList(),
                            swappedIndices = setOf(k),
                            description = "${leftArray[i]} > ${rightArray[j]} → берём элемент из правой части"
                        )
                    )
                    j++
                }
                k++
            }

            while (i < leftArray.size) {
                mutableArray[k] = leftArray[i]
                steps.add(
                    VisualizationStep(
                        stepNumber = steps.size,
                        array = mutableArray.toList(),
                        swappedIndices = setOf(k),
                        description = "Копируем оставшийся элемент из левой части: ${leftArray[i]}"
                    )
                )
                i++
                k++
            }

            while (j < rightArray.size) {
                mutableArray[k] = rightArray[j]
                steps.add(
                    VisualizationStep(
                        stepNumber = steps.size,
                        array = mutableArray.toList(),
                        swappedIndices = setOf(k),
                        description = "Копируем оставшийся элемент из правой части: ${rightArray[j]}"
                    )
                )
                j++
                k++
            }

            steps.add(
                VisualizationStep(
                    stepNumber = steps.size,
                    array = mutableArray.toList(),
                    sortedIndices = (left..right).toSet(),
                    description = "Слияние завершено! Диапазон [${left}..${right}] отсортирован"
                )
            )
        }

        suspend fun mergeSort(left: Int, right: Int) {
            if (left < right) {
                val mid = left + (right - left) / 2

                steps.add(
                    VisualizationStep(
                        stepNumber = steps.size,
                        array = mutableArray.toList(),
                        highlightedIndices = (left..right).toSet(),
                        description = "Рекурсивно сортируем левую половину [${left}..${mid}]"
                    )
                )
                mergeSort(left, mid)

                steps.add(
                    VisualizationStep(
                        stepNumber = steps.size,
                        array = mutableArray.toList(),
                        highlightedIndices = (left..right).toSet(),
                        description = "Рекурсивно сортируем правую половину [${mid + 1}..${right}]"
                    )
                )
                mergeSort(mid + 1, right)

                steps.add(
                    VisualizationStep(
                        stepNumber = steps.size,
                        array = mutableArray.toList(),
                        highlightedIndices = (left..right).toSet(),
                        description = "Сливаем две отсортированные половины"
                    )
                )
                merge(left, mid, right)
            }
        }

        mergeSort(0, mutableArray.size - 1)

        steps.add(
            VisualizationStep(
                stepNumber = steps.size,
                array = mutableArray.toList(),
                sortedIndices = mutableArray.indices.toSet(),
                description = "Массив полностью отсортирован! Merge Sort завершён."
            )
        )

        emit(steps)
    }

    override fun getDefaultInput(): List<Int> {
        return listOf(38, 27, 43, 3, 9, 82, 10)
    }

    override fun getInputDescription(): String {
        return "Массив целых чисел для сортировки слиянием (рекомендуется 7-12 элементов)"
    }
}