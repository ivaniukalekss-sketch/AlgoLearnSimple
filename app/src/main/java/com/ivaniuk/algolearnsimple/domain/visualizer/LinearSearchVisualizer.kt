package com.ivaniuk.algolearnsimple.domain.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import com.ivaniuk.algolearnsimple.domain.model.DataGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class LinearSearchVisualizer : AlgorithmVisualizer {
    override val algorithmId: Int = 10
    override val algorithmName: String = "Linear Search"
    override val algorithmType: AlgorithmType = AlgorithmType.SEARCHING_ARRAY

    override fun generateRandomInput(): Any {
        val array = DataGenerator.generateRandomArray(8)
        val target = if (Random.nextBoolean()) array.random() else -1
        return Pair(array, target)
    }

    override fun visualize(input: Any): Flow<List<VisualizationStep>> = flow {
        val data = when (input) {
            is Pair<*, *> -> {
                val list = (input.first as? List<*>)?.filterIsInstance<Int>() ?: getDefaultInput().first
                val target = (input.second as? Int) ?: getDefaultInput().second
                Pair(list, target)
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
                description = "Начинаем линейный поиск элемента $target в массиве из ${array.size} элементов"
            )
        )

        var foundIndex = -1

        for (i in array.indices) {
            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    array = array,
                    comparingIndices = setOf(i),
                    description = "Проверяем элемент на позиции $i: ${array[i]} ?= $target"
                )
            )

            if (array[i] == target) {
                foundIndex = i
                steps.add(
                    VisualizationStep(
                        stepNumber = stepCounter++,
                        array = array,
                        highlightedIndices = setOf(i),
                        description = "✅ Элемент $target найден на позиции $i!"
                    )
                )
                break
            } else {
                steps.add(
                    VisualizationStep(
                        stepNumber = stepCounter++,
                        array = array,
                        comparingIndices = setOf(i),
                        description = "❌ ${array[i]} не равно $target, продолжаем поиск"
                    )
                )
            }
        }

        if (foundIndex == -1) {
            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    array = array,
                    description = "Элемент $target не найден в массиве"
                )
            )
        }

        emit(steps)
    }

    override fun getDefaultInput(): Pair<List<Int>, Int> {
        return Pair(listOf(3, 7, 1, 9, 5, 2, 8, 4, 6), 5)
    }

    override fun getInputDescription(): String {
        return "Массив целых чисел и элемент для поиска. Алгоритм проверяет каждый элемент по порядку."
    }
}