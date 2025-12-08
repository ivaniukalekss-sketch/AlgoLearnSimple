package com.ivaniuk.algolearnsimple.data.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BubbleSortVisualizer : AlgorithmVisualizer {
    override val algorithmId: Int = 1
    override val algorithmName: String = "Bubble Sort"
    override val algorithmType: AlgorithmType = AlgorithmType.SORTING_ARRAY

    override fun visualize(input: Any): Flow<List<VisualizationStep>> = flow {
        val array = input as? List<Int> ?: getDefaultInput() as List<Int>
        val steps = mutableListOf<VisualizationStep>()
        val mutableArray = array.toMutableList()
        val n = mutableArray.size

        steps.add(
            VisualizationStep(
                stepNumber = 0,
                array = mutableArray.toList(),
                description = "Начальный массив: ${array.joinToString()}"
            )
        )

        var stepCounter = 1

        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                steps.add(
                    VisualizationStep(
                        stepNumber = stepCounter++,
                        array = mutableArray.toList(),
                        comparingIndices = setOf(j, j + 1),
                        sortedIndices = (n - i until n).toSet(),
                        description = "Сравниваем элементы [${j}] = ${mutableArray[j]} и [${j + 1}] = ${mutableArray[j + 1]}"
                    )
                )

                if (mutableArray[j] > mutableArray[j + 1]) {
                    val temp = mutableArray[j]
                    mutableArray[j] = mutableArray[j + 1]
                    mutableArray[j + 1] = temp

                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            array = mutableArray.toList(),
                            swappedIndices = setOf(j, j + 1),
                            sortedIndices = (n - i until n).toSet(),
                            description = "Меняем местами ${mutableArray[j + 1]} и ${mutableArray[j]}"
                        )
                    )
                }
            }

            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    array = mutableArray.toList(),
                    sortedIndices = (n - i - 1 until n).toSet(),
                    description = "Элемент на позиции ${n - i - 1} теперь на своём месте"
                )
            )
        }

        steps.add(
            VisualizationStep(
                stepNumber = stepCounter,
                array = mutableArray.toList(),
                sortedIndices = mutableArray.indices.toSet(),
                description = "Массив отсортирован!"
            )
        )

        emit(steps)
    }

    override fun getDefaultInput(): Any {
        return listOf(5, 3, 8, 4, 2, 7, 1, 6)
    }

    override fun getInputDescription(): String {
        return "Массив целых чисел для сортировки"
    }
}