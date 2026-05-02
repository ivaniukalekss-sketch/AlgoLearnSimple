package com.ivaniuk.algolearnsimple.domain.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import com.ivaniuk.algolearnsimple.domain.model.DataGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertionSortVisualizer : AlgorithmVisualizer {
    override val algorithmId: Int = 9
    override val algorithmName: String = "Insertion Sort"
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
                description = "Начинаем Insertion Sort (сортировка вставками): ${mutableArray.joinToString()}"
            )
        )

        var stepCounter = 1

        for (i in 1 until mutableArray.size) {
            val key = mutableArray[i]
            var j = i - 1

            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    array = mutableArray.toList(),
                    highlightedIndices = setOf(i),
                    description = "Берём элемент ${mutableArray[i]} на позиции $i для вставки"
                )
            )

            while (j >= 0 && mutableArray[j] > key) {
                steps.add(
                    VisualizationStep(
                        stepNumber = stepCounter++,
                        array = mutableArray.toList(),
                        comparingIndices = setOf(j, j + 1),
                        description = "${mutableArray[j]} > $key → сдвигаем ${mutableArray[j]} вправо"
                    )
                )

                mutableArray[j + 1] = mutableArray[j]
                j--
            }

            mutableArray[j + 1] = key

            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    array = mutableArray.toList(),
                    sortedIndices = (0..i).toSet(),
                    description = "Вставляем $key на позицию ${j + 1}. Теперь отсортировано [0..$i]"
                )
            )
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
        return listOf(12, 11, 13, 5, 6)
    }

    override fun getInputDescription(): String {
        return "Массив целых чисел. Алгоритм берёт элемент и вставляет его в отсортированную часть."
    }
}