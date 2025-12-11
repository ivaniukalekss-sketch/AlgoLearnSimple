package com.ivaniuk.algolearnsimple.domain.visualizer

import com.ivaniuk.algolearnsimple.domain.model.AlgorithmType
import com.ivaniuk.algolearnsimple.domain.model.VisualizationStep
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmVisualizer
import com.ivaniuk.algolearnsimple.domain.util.DataGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QuickSortVisualizer : AlgorithmVisualizer {
    override val algorithmId: Int = 3
    override val algorithmName: String = "Quick Sort"
    override val algorithmType: AlgorithmType = AlgorithmType.SORTING_ARRAY

    override fun generateRandomInput(): Any {
        return DataGenerator.generateRandomArray()
    }

    override fun visualize(input: Any): Flow<List<VisualizationStep>> = flow {
        val array = input as? List<Int> ?: getDefaultInput() as List<Int>
        val steps = mutableListOf<VisualizationStep>()
        val mutableArray = array.toMutableList()

        fun partition(arr: MutableList<Int>, low: Int, high: Int, parentStack: ArrayDeque<Pair<Int, Int>>): Int {
            val pivot = arr[high]
            var i = low - 1

            steps.add(
                VisualizationStep(
                    stepNumber = steps.size,
                    array = arr.toList(),
                    highlightedIndices = setOf(high),
                    description = "Выбираем опорный элемент (pivot): ${arr[high]} на позиции $high",
                    codeLine = "val pivot = arr[$high]"
                )
            )

            for (j in low until high) {
                steps.add(
                    VisualizationStep(
                        stepNumber = steps.size,
                        array = arr.toList(),
                        comparingIndices = setOf(j, high),
                        highlightedIndices = parentStack.flatMap { (l, h) -> (l..h) }.toSet(),
                        description = "Сравниваем arr[$j] = ${arr[j]} с pivot = $pivot"
                    )
                )

                if (arr[j] < pivot) {
                    i++

                    if (i != j) {
                        steps.add(
                            VisualizationStep(
                                stepNumber = steps.size,
                                array = arr.toList(),
                                swappedIndices = setOf(i, j),
                                highlightedIndices = parentStack.flatMap { (l, h) -> (l..h) }.toSet(),
                                description = "arr[$j] < $pivot → меняем местами arr[$i] и arr[$j]"
                            )
                        )

                        arr[i] = arr[j].also { arr[j] = arr[i] }

                        steps.add(
                            VisualizationStep(
                                stepNumber = steps.size,
                                array = arr.toList(),
                                highlightedIndices = parentStack.flatMap { (l, h) -> (l..h) }.toSet(),
                                description = "После обмена: arr[$i] = ${arr[i]}, arr[$j] = ${arr[j]}"
                            )
                        )
                    }
                }
            }

            if (i + 1 != high) {
                steps.add(
                    VisualizationStep(
                        stepNumber = steps.size,
                        array = arr.toList(),
                        swappedIndices = setOf(i + 1, high),
                        highlightedIndices = parentStack.flatMap { (l, h) -> (l..h) }.toSet(),
                        description = "Помещаем pivot на правильную позицию: меняем arr[${i + 1}] и arr[$high]"
                    )
                )

                arr[i + 1] = arr[high].also { arr[high] = arr[i + 1] }
            }

            steps.add(
                VisualizationStep(
                    stepNumber = steps.size,
                    array = arr.toList(),
                    highlightedIndices = setOf(i + 1),
                    sortedIndices = setOf(i + 1),
                    description = "Pivot $pivot теперь на позиции ${i + 1} (на своём месте)",
                    codeLine = "return ${i + 1}  // позиция pivot"
                )
            )

            return i + 1
        }

        fun quickSort(arr: MutableList<Int>, low: Int, high: Int, stack: ArrayDeque<Pair<Int, Int>> = ArrayDeque()) {
            if (low < high) {
                stack.addLast(low to high)

                steps.add(
                    VisualizationStep(
                        stepNumber = steps.size,
                        array = arr.toList(),
                        highlightedIndices = (low..high).toSet(),
                        description = "Рекурсивный вызов: сортируем подмассив [$low, $high] (${arr.slice(low..high).joinToString()})",
                        codeLine = "quickSort(arr, $low, $high)"
                    )
                )

                val pi = partition(arr, low, high, stack)

                steps.add(
                    VisualizationStep(
                        stepNumber = steps.size,
                        array = arr.toList(),
                        highlightedIndices = (low until pi).toSet(),
                        description = "Левый подмассив: [$low, ${pi - 1}]"
                    )
                )
                quickSort(arr, low, pi - 1, stack)

                steps.add(
                    VisualizationStep(
                        stepNumber = steps.size,
                        array = arr.toList(),
                        highlightedIndices = (pi + 1..high).toSet(),
                        description = "Правый подмассив: [${pi + 1}, $high]"
                    )
                )
                quickSort(arr, pi + 1, high, stack)

                stack.removeLast()
            } else if (low == high) {
                steps.add(
                    VisualizationStep(
                        stepNumber = steps.size,
                        array = arr.toList(),
                        sortedIndices = setOf(low),
                        description = "Подмассив из одного элемента arr[$low] = ${arr[low]} уже отсортирован"
                    )
                )
            }
        }

        steps.add(
            VisualizationStep(
                stepNumber = 0,
                array = mutableArray.toList(),
                description = "Начинаем Quick Sort массива: ${mutableArray.joinToString()}"
            )
        )

        quickSort(mutableArray, 0, mutableArray.size - 1)

        steps.add(
            VisualizationStep(
                stepNumber = steps.size,
                array = mutableArray.toList(),
                sortedIndices = mutableArray.indices.toSet(),
                description = "Массив полностью отсортирован! Quick Sort завершён."
            )
        )

        emit(steps)
    }

    override fun getDefaultInput(): Any {
        return listOf(10, 7, 8, 9, 1, 5, 3, 6, 4, 2)
    }

    override fun getInputDescription(): String {
        return "Массив целых чисел для сортировки (рекомендуется 8-12 элементов)"
    }
}