package com.ivaniuk.algolearnsimple.data.repository

import com.ivaniuk.algolearnsimple.domain.model.Algorithm
import com.ivaniuk.algolearnsimple.domain.model.AlgorithmCategory
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class AlgorithmRepositoryImpl : AlgorithmRepository {


    private val initialAlgorithms = listOf(
        Algorithm(
            id = 1,
            title = "Bubble Sort",
            description = "Простейший алгоритм сортировки, который многократно проходит по списку, сравнивая соседние элементы и меняя их местами, если они находятся в неправильном порядке.",
            category = AlgorithmCategory.SORTING,
            complexity = "O(n²)",
            codeExample = """
                fun bubbleSort(arr: IntArray) {
                    val n = arr.size
                    for (i in 0 until n - 1) {
                        for (j in 0 until n - i - 1) {
                            if (arr[j] > arr[j + 1]) {
                                val temp = arr[j]
                                arr[j] = arr[j + 1]
                                arr[j + 1] = temp
                            }
                        }
                    }
                }
            """.trimIndent(),
            steps = listOf(
                "Начинаем с первого элемента",
                "Сравниваем текущий элемент со следующим",
                "Если текущий больше следующего - меняем местами",
                "Переходим к следующей паре",
                "Повторяем до конца массива"
            ),
            isFavorite = true
        ),
        Algorithm(
            id = 2,
            title = "Binary Search",
            description = "Эффективный алгоритм поиска элемента в отсортированном массиве. Работает по принципу 'разделяй и властвуй'.",
            category = AlgorithmCategory.SEARCHING,
            complexity = "O(log n)",
            codeExample = """
                fun binarySearch(arr: IntArray, target: Int): Int {
                    var left = 0
                    var right = arr.size - 1
                    
                    while (left <= right) {
                        val mid = left + (right - left) / 2
                        
                        when {
                            arr[mid] == target -> return mid
                            arr[mid] < target -> left = mid + 1
                            else -> right = mid - 1
                        }
                    }
                    return -1
                }
            """.trimIndent(),
            steps = listOf(
                "Проверяем середину массива",
                "Если элемент найден - возвращаем индекс",
                "Если искомый элемент меньше среднего - ищем в левой половине",
                "Если больше - ищем в правой половине",
                "Повторяем пока не найдём элемент"
            ),
            isFavorite = true
        ),
        Algorithm(
            id = 3,
            title = "Quick Sort",
            description = "Эффективный алгоритм сортировки, использующий стратегию 'разделяй и властвуй'. Выбирается опорный элемент, и массив разделяется на две части.",
            category = AlgorithmCategory.SORTING,
            complexity = "O(n log n)",
            codeExample = """
                fun quickSort(arr: IntArray, low: Int = 0, high: Int = arr.size - 1) {
                    if (low < high) {
                        val pi = partition(arr, low, high)
                        quickSort(arr, low, pi - 1)
                        quickSort(arr, pi + 1, high)
                    }
                }
            """.trimIndent(),
            steps = listOf(
                "Выбираем опорный элемент",
                "Разделяем массив на две части",
                "В левой - элементы меньше опорного",
                "В правой - элементы больше опорного",
                "Рекурсивно сортируем обе части"
            ),
            isFavorite = false
        ),
        Algorithm(
            id = 4,
            title = "DFS (Depth-First Search)",
            description = "Алгоритм обхода или поиска в структурах данных типа дерева или графа. Исследует ветви графа настолько глубоко, насколько это возможно.",
            category = AlgorithmCategory.GRAPH,
            complexity = "O(V + E)",
            codeExample = """
                fun dfs(graph: Map<Int, List<Int>>, start: Int) {
                    val visited = mutableSetOf<Int>()
                    val stack = ArrayDeque<Int>()
                    
                    stack.addLast(start)
                    
                    while (stack.isNotEmpty()) {
                        val vertex = stack.removeLast()
                        if (vertex !in visited) {
                            visited.add(vertex)
                            
                            graph[vertex]?.forEach { neighbor ->
                                if (neighbor !in visited) {
                                    stack.addLast(neighbor)
                                }
                            }
                        }
                    }
                }
            """.trimIndent(),
            steps = listOf(
                "Начинаем с начальной вершины",
                "Помечаем вершину как посещённую",
                "Для каждой соседней вершины",
                "Если не посещена - рекурсивно вызываем DFS"
            ),
            isFavorite = true
        )
    )


    private val _algorithms = MutableStateFlow(initialAlgorithms)

    override fun getAllAlgorithms(): Flow<List<Algorithm>> = _algorithms

    override fun getAlgorithmsByCategory(category: String): Flow<List<Algorithm>> =
        _algorithms.map { algorithms ->
            algorithms.filter { it.category.name == category }
        }

    override fun getFavoriteAlgorithms(): Flow<List<Algorithm>> =
        _algorithms.map { algorithms ->
            algorithms.filter { it.isFavorite }
        }

    override suspend fun toggleFavorite(algorithmId: Int) {
        delay(50)

        _algorithms.update { currentAlgorithms ->
            currentAlgorithms.map { algorithm ->
                if (algorithm.id == algorithmId) {
                    algorithm.copy(isFavorite = !algorithm.isFavorite)
                } else {
                    algorithm
                }
            }
        }
    }

    override suspend fun getAlgorithmById(id: Int): Algorithm? {
        delay(30)
        return _algorithms.value.find { it.id == id }
    }
}