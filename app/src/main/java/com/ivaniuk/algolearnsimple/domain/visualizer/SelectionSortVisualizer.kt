package com.ivaniuk.algolearnsimple.domain.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import com.ivaniuk.algolearnsimple.domain.model.DataGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SelectionSortVisualizer : AlgorithmVisualizer {
    override val algorithmId: Int = 8
    override val algorithmName: String = "Selection Sort"
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
                description = "Начинаем Selection Sort (сортировка выбором): ${mutableArray.joinToString()}"
            )
        )

        var stepCounter = 1

        for (i in 0 until mutableArray.size - 1) {
            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    array = mutableArray.toList(),
                    description = "Ищем минимальный элемент в диапазоне [${i}..${mutableArray.size - 1}]"
                )
            )

            var minIndex = i
            for (j in i + 1 until mutableArray.size) {
                steps.add(
                    VisualizationStep(
                        stepNumber = stepCounter++,
                        array = mutableArray.toList(),
                        comparingIndices = setOf(j, minIndex),
                        highlightedIndices = (i until mutableArray.size).toSet(),
                        description = "Сравниваем ${mutableArray[j]} с текущим минимумом ${mutableArray[minIndex]}"
                    )
                )

                if (mutableArray[j] < mutableArray[minIndex]) {
                    minIndex = j
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            array = mutableArray.toList(),
                            highlightedIndices = setOf(minIndex),
                            description = "Новый минимальный элемент: ${mutableArray[minIndex]} на позиции $minIndex"
                        )
                    )
                }
            }

            if (minIndex != i) {
                steps.add(
                    VisualizationStep(
                        stepNumber = stepCounter++,
                        array = mutableArray.toList(),
                        swappedIndices = setOf(i, minIndex),
                        description = "Меняем местами элементы на позициях $i и $minIndex"
                    )
                )

                val temp = mutableArray[i]
                mutableArray[i] = mutableArray[minIndex]
                mutableArray[minIndex] = temp

                steps.add(
                    VisualizationStep(
                        stepNumber = stepCounter++,
                        array = mutableArray.toList(),
                        sortedIndices = (0..i).toSet(),
                        description = "Элемент на позиции $i теперь на своём месте: ${mutableArray[i]}"
                    )
                )
            }
        }

        steps.add(
            VisualizationStep(
                stepNumber = stepCounter++,
                array = mutableArray.toList(),
                sortedIndices = mutableArray.indices.toSet(),
                description = "Массив полностью отсортирован!"
            )
        )

        emit(steps)
    }

    override fun getDefaultInput(): List<Int> {
        return listOf(64, 25, 12, 22, 11)
    }

    override fun getInputDescription(): String {
        return "Массив целых чисел. Алгоритм на каждом шаге выбирает минимальный элемент и ставит его в начало."
    }
}