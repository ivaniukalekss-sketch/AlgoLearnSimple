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

        // Шаг 0: Исходный массив
        steps.add(
            VisualizationStep(
                stepNumber = 0,
                array = mutableArray.toList(),
                description = "Начальный массив: ${array.joinToString()}"
            )
        )

        var stepCounter = 1

        for (i in 0 until n - 1) {
            // Шаг: Начало нового прохода
            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    array = mutableArray.toList(),
                    description = "Начинаем проход ${i + 1} из ${n - 1}"
                )
            )

            for (j in 0 until n - i - 1) {
                // Шаг 1: Подсветка элементов для сравнения
                steps.add(
                    VisualizationStep(
                        stepNumber = stepCounter++,
                        array = mutableArray.toList(),
                        comparingIndices = setOf(j, j + 1),
                        sortedIndices = (n - i until n).toSet(),
                        description = "Сравниваем элементы [${j}] = ${mutableArray[j]} и [${j + 1}] = ${mutableArray[j + 1]}",
                        codeLine = "if (arr[$j] > arr[${j + 1}])"
                    )
                )

                if (mutableArray[j] > mutableArray[j + 1]) {
                    // Шаг 2: Подготовка к обмену (подсветка красным)
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            array = mutableArray.toList(),
                            comparingIndices = setOf(j, j + 1),
                            swappedIndices = setOf(j, j + 1), // Оба красные
                            sortedIndices = (n - i until n).toSet(),
                            description = "${mutableArray[j]} > ${mutableArray[j + 1]} → готовимся к обмену"
                        )
                    )

                    // Обмен значений
                    val temp = mutableArray[j]
                    mutableArray[j] = mutableArray[j + 1]
                    mutableArray[j + 1] = temp

                    // Шаг 3: После обмена (один элемент переместился)
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            array = mutableArray.toList(),
                            swappedIndices = setOf(j, j + 1),
                            sortedIndices = (n - i until n).toSet(),
                            description = "Обменяли местами: теперь arr[$j] = ${mutableArray[j]}, arr[${j + 1}] = ${mutableArray[j + 1]}",
                            codeLine = "val temp = arr[$j]; arr[$j] = arr[${j + 1}]; arr[${j + 1}] = temp"
                        )
                    )

                    // Шаг 4: Завершение обмена (сброс цветов)
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            array = mutableArray.toList(),
                            sortedIndices = (n - i until n).toSet(),
                            description = "Обмен завершён"
                        )
                    )
                } else {
                    // Шаг 2а: Элементы в правильном порядке
                    steps.add(
                        VisualizationStep(
                            stepNumber = stepCounter++,
                            array = mutableArray.toList(),
                            comparingIndices = setOf(j, j + 1),
                            sortedIndices = (n - i until n).toSet(),
                            description = "${mutableArray[j]} ≤ ${mutableArray[j + 1]} → порядок правильный, идём дальше"
                        )
                    )
                }
            }

            // Шаг: Элемент на своём месте
            steps.add(
                VisualizationStep(
                    stepNumber = stepCounter++,
                    array = mutableArray.toList(),
                    sortedIndices = (n - i - 1 until n).toSet(),
                    description = "Элемент на позиции ${n - i - 1} теперь на своём месте (значение = ${mutableArray[n - i - 1]})"
                )
            )
        }

        // Финальный шаг: Весь массив отсортирован
        steps.add(
            VisualizationStep(
                stepNumber = stepCounter,
                array = mutableArray.toList(),
                sortedIndices = mutableArray.indices.toSet(),
                description = "✅ Массив полностью отсортирован!"
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